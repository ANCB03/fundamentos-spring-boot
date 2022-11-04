package com.fundamentos.springboot.fundamentos;

import com.fundamentos.springboot.fundamentos.bean.MyBean;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentos.springboot.fundamentos.bean.MyOperationImplement;
import com.fundamentos.springboot.fundamentos.component.ComponentDependency;
import com.fundamentos.springboot.fundamentos.component.ComponentImplement;
import com.fundamentos.springboot.fundamentos.entity.User;
import com.fundamentos.springboot.fundamentos.pojo.UserPojo;
import com.fundamentos.springboot.fundamentos.repository.UserRepository;
import com.fundamentos.springboot.fundamentos.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {
	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	private ComponentDependency componentDependency;
	private MyBean myBean;
	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;
	private UserRepository userRepository;
	private UserService userService;
	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency,MyBean myBean,MyBeanWithDependency myBeanWithDependency,MyBeanWithProperties myBeanWithProperties,UserPojo userPojo,UserRepository userRepository,UserService userService){
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}
	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args)  {
		//ejemploAnteriores();
		saveUsersInDataBase();
		getInformationJpqlFromUser();
		saveWithErrorTransactional();

	}

	private void saveWithErrorTransactional(){
		User test1 = new User("TestTransactional1","TestTransactional1@domain.com",LocalDate.now());
		User test2 = new User("TestTransactional2","TestTransactional2@domain.com",LocalDate.now());
		User test3 = new User("TestTransactional3","TestTransactional1@domain.com",LocalDate.now());
		User test4 = new User("TestTransactional4","TestTransactional4@domain.com",LocalDate.now());
		List<User> users = Arrays.asList(test1,test2,test3,test4);
		try {
			userService.saveTransactional(users);
		}catch(Exception e){
			LOGGER.error("Esta es una excepcion dentro del metodo transactional" + e);
		}
		userService.getAllUsers().stream()
				.forEach(user -> LOGGER.info("Este es el usuario dentro del metodo transactional: " + user));
	}

	private void getInformationJpqlFromUser(){
		/*LOGGER.info("Usuario con el método findByUserEmail: " +
				userRepository.findByUserEmail("andres@gmail.com")
						.orElseThrow(()->new RuntimeException("No se encontró el usuario")));

		userRepository.findAndSort("user", Sort.by("id").descending())
				.stream()
				.forEach(user -> LOGGER.info("Usuario con metodo sort: " +user));

		userRepository.findByName("angela").stream()
				.forEach(user -> LOGGER.info("usuario con QuerryMethod: " + user));

		LOGGER.info("Usuario con QuerryMethod con findByEmailAndName" +
				userRepository.findByEmailAndName("angela@gmail.com","angela")
				.orElseThrow(()-> new RuntimeException("usuario no encontrado")));

		userRepository.findByNameLike("%u%")
				.stream().forEach(user -> LOGGER.info("usuario findByNameLike " + user));

		userRepository.findByNameOrEmail(null,"jose@gmail.com")
				.stream().forEach(user -> LOGGER.info("usuario findByNameOrEmail " + user)); */

		userRepository.findByBirthDateBetween(LocalDate.of(2022,1,1),LocalDate.of(2022,12,31)).stream()
				.forEach(user -> LOGGER.info("Usuario con intervalo de fechas " + user));

		userRepository.findByNameLikeOrderByIdDesc("%user%").stream()
				.forEach(user -> LOGGER.info("usuario encontrado con like y ordenado" + user));

		LOGGER.info("El usuario a partir del named parameter es: "+userRepository.getAllByBirthDateAndEmail(LocalDate.of(2022,10,1),"ana@gmail.com")
				.orElseThrow(()-> new RuntimeException("no se encontro el usuario a partir del parameter")));
	}

	private void saveUsersInDataBase(){
		User user1 = new User("Andres","andres@gmail.com", LocalDate.of(2022,10,1));
		User user2 = new User("juan","juan@gmail.com", LocalDate.of(2021,11,21));
		User user3 = new User("ana","ana@gmail.com", LocalDate.of(2022,10,1));
		User user4 = new User("angela","angela@gmail.com", LocalDate.of(2021,01,21));
		User user5 = new User("Andres4","4andres@gmail.com", LocalDate.of(2022,06,1));
		User user6 = new User("pepito","pepito@gmail.com", LocalDate.of(2021,8,21));
		User user7 = new User("casandra","casandra@gmail.com", LocalDate.of(2022,10,1));
		User user8 = new User("juanito","juanito@gmail.com", LocalDate.of(2021,11,21));
		User user9 = new User("jose","jose@gmail.com", LocalDate.of(2022,10,1));
		User user10 = new User("amon","amon@gmail.com", LocalDate.of(2021,11,21));
		List<User> list = Arrays.asList(user1,user2,user3,user4,user5,user6,user7,user8,user9,user10);
		list.stream().forEach(userRepository::save);
	}

	private void ejemploAnteriores(){
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail()+"-"+userPojo.getPassword());
		try{
			int value = 10/0;
			LOGGER.debug("Mi valor es:" + value);

		}catch(Exception e){
			LOGGER.error("ESTO ES UN ERROR AL DIVIDIR POR 0" + e.getStackTrace());
		}
	}
}

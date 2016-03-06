package com.tony.jpa.helloworld;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {
	public static void main(String[] args) {

		// 1. Create EntityManagerFactor
		String persistenceUnitName = "jpa-1";
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);

		// 2. Create EntityManger
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		// 3. Open transaction
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		// 4. Run persistence execution
		Customer customer = new Customer();
		customer.setAge(12);
		customer.setEmail("abc@gmail.com");
		customer.setLastName("Tom");
		customer.setBrith(new Date());
		customer.setCreatedTime(new Date());

		entityManager.persist(customer);

		// 5. Submit transaction
		transaction.commit();

		// 6. Close EntityManager
		entityManager.close();

		// 7. Close EntityMangerFactory
		entityManagerFactory.close();
	}
}

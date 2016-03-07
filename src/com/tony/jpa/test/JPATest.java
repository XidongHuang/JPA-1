package com.tony.jpa.test;

import static org.junit.Assert.*;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hibernate.dialect.CUBRIDDialect;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tony.jpa.helloworld.Customer;

public class JPATest {

	private EntityManager entityManager;
	private EntityManagerFactory entityManagerFactory;
	private EntityTransaction transaction;
	
	
	@Before
	public void init(){
		entityManagerFactory = Persistence.createEntityManagerFactory("jpa-1");
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
		
		
	}
	
	@After
	public void destory(){
		transaction.commit();
		entityManager.close();
		entityManagerFactory.close();
		
		
	}
	
	
	//Similar with hibernate's delete method
	@Test
	public void testRemove(){
		
//		Customer customer = new Customer();
//		customer.setId(2);
		
		Customer customer = entityManager.find(Customer.class, 3);
		
		entityManager.remove(customer);
		
		
	}
	
	
	//Similar with hibernate's "save" method.
	//The different that compares with Hibernate's save method: if the entity has "id", then throw an exception
	@Test
	public void testPersistence(){
		
		Customer customer = new Customer();
		customer.setAge(15);
		customer.setBrith(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("bb@gmail.com");
		customer.setLastName("BB");
		
		entityManager.persist(customer);
		System.out.println(customer.getInfo());
		
	}
	
	
	
	//Similar with hibernate's Session "load" method
	@Test
	public void testGetReference(){
		
		
		Customer customer = entityManager.getReference(Customer.class, 1);
		System.out.println(customer.getClass().getName());
		System.out.println(customer.getClass().getName());
		System.out.println("------------------------------------");
		System.out.println(customer);
		
	}
	
	
	//Similar with hibernate's Session "get" method
	@Test
	public void testFind() {

		Customer customer = entityManager.find(Customer.class, 1);
		System.out.println("------------------------------------");
		System.out.println(customer);
		
		
	}

}

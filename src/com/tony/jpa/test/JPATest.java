package com.tony.jpa.test;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.jpa.QueryHints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tony.jpa.helloworld.Category;
import com.tony.jpa.helloworld.Customer;
import com.tony.jpa.helloworld.Department;
import com.tony.jpa.helloworld.Item;
import com.tony.jpa.helloworld.Manager;
import com.tony.jpa.helloworld.Order;

public class JPATest {

	private EntityManager entityManager;
	private EntityManagerFactory entityManagerFactory;
	private EntityTransaction transaction;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory("jpa-1");
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();

	}

	@After
	public void destory() {
		transaction.commit();
		entityManager.close();
		entityManagerFactory.close();

	}

	@Test
	public void testExecuteUpdate(){
		String jpql = "UPDATE Customer c SET c.lastName = ? WHERE c.id = ?";
		
		Query query = entityManager.createQuery(jpql).setParameter(1, "KKK").setParameter(2, 12);
		
		query.executeUpdate();
		
		
	}
	
	
	@Test
	public void testJPQLFunction(){
		
		String jpql = "SELECT lower(c.email) FROM Customer c";
		
		List<String> emails = entityManager.createQuery(jpql).getResultList();
		
		System.out.println(emails);
		
	}
	
	@Test
	public void testSubQuery(){
		//Query all Order which Customer's lastName is YY
		String jpql = "SELECT o FROM Order o WHERE o.customer = (SELECT c FROM Customer c WHERE c.lastName = ?)";
		
		Query query = entityManager.createQuery(jpql).setParameter(1, "KK");
		
		List<Order> orders = query.getResultList();
		System.out.println(orders.size());
		
	}
	
	
	/**
	 * JPQL relation querying is same as HQL's.
	 * 
	 * 
	 */
	@Test
	public void testLeftOuterJoinFetch(){
		
		String jpql = "FROM Customer c LEFT OUTER JOIN FETCH c.orders WHERE c.id = ?";
		
		Customer customer = (Customer) entityManager.createQuery(jpql).setParameter(1, 12).getSingleResult();
		
		System.out.println(customer.getLastName());
		
		System.out.println(customer.getOrders().size());
		
//		List<Object[]> result = entityManager.createQuery(jpql).setParameter(1, 12).getResultList();
//		
//		System.out.println(result);
		
	}
	
	
	
	//Query the customers whose order is larger than 2
	@Test
	public void testOrderBy(){
		
		String jpql = " SELECT o.customer FROM Order o GROUP BY o.customer HAVING count(o.id) > 2";

		List<Customer> customers = entityManager.createQuery(jpql).getResultList();
		
		System.out.println(customers);
		
		
	}
	
	
	
	@Test
	public void testQueryCache() {

		String jpql = "FROM Customer c WHERE c.age > ?";
		Query query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);

		query.setParameter(1, 1);

		List<Customer> customers = query.getResultList();
		System.out.println(customers.size());

		query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);

		query.setParameter(1, 1);

		customers = query.getResultList();
		System.out.println(customers.size());

	}

	// createNativeQuery fits for native SQL
	@Test
	public void testNativeQuery() {
		String sql = "SELECT age FROM JPA_CUSTOMER WHERE id = ?";
		Query query = entityManager.createNativeQuery(sql).setParameter(1, 4);

		Object reObject = query.getSingleResult();

		System.out.println(reObject);

	}

	// createNamedQuery fits for real classes that have @NamedQuery
	@Test
	public void testNamedQuery() {
		Query query = entityManager.createNamedQuery("testNamedQuery").setParameter(1, 4);

		Customer customer = (Customer) query.getSingleResult();

		System.out.println(customer);
	}

	// In default, if just query parts of attributes, then it will return
	// Object[] result. Or the list of Object[]
	// Or can use Constructor in real class, then nest in JPQL to return the
	// real class object
	@Test
	public void testPartlyProperties() {

		String jpql = "SELECT new Customer(c.lastName, c.age) FROM Customer c WHERE c.id > ?";

		List reList = entityManager.createQuery(jpql).setParameter(1, 1).getResultList();

		System.out.println(reList);

	}

	// PlaceHolder starts from "1"
	@Test
	public void testHelloJPQL() {
		String jpql = "FROM Customer c WHERE c.age > ?";
		Query query = entityManager.createQuery(jpql);

		query.setParameter(1, 1);
		List<Customer> customers = query.getResultList();
		System.out.println(customers.size());
	}

	@Test
	public void testSecondLevelCache() {
		Customer customer1 = entityManager.find(Customer.class, 1);

		transaction.commit();
		entityManager.close();

		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();

		Customer customer2 = entityManager.find(Customer.class, 1);
		System.out.println(customer1.getLastName());

	}

	@Test
	public void testManyToManyFind() {
		// Item item = entityManager.find(Item.class, 22);
		// System.out.println(item.getItemName());
		//
		// System.out.println(item.getCategories());

		Category category = entityManager.find(Category.class, 24);
		System.out.println(category.getCategoryName());
		System.out.println(category.getItems().size());

	}

	// ManyToMany persistence
	@Test
	public void testManyToManyPersistence() {

		Item i1 = new Item();
		i1.setItemName("i-1");

		Item i2 = new Item();
		i2.setItemName("i-2");
		;

		Category c1 = new Category();
		c1.setCategoryName("C-1");

		Category c2 = new Category();
		c2.setCategoryName("C-2");

		i1.getCategories().add(c1);
		i1.getCategories().add(c2);

		i2.getCategories().add(c1);
		i2.getCategories().add(c2);

		c1.getItems().add(i1);
		c1.getItems().add(i2);

		c2.getItems().add(i1);
		c2.getItems().add(i2);

		entityManager.persist(i1);
		entityManager.persist(i2);
		entityManager.persist(c1);
		entityManager.persist(c2);

	}

	// In default, if gain the side that does not maintain, then will use left
	// outer join to gain its related object
	// Can use @OneToOne's fetch attribute to modify strategy. But still keep
	// sending SQL to initial its related objects
	// So it is unnecessary to set "fetch" in the side that does not maintain
	@Test
	public void testOneToOneFind2() {

		Manager mgr = entityManager.find(Manager.class, 18);
		System.out.println(mgr.getMgrName());

		System.out.println(mgr.getDept().getClass().getName());

	}

	// In default
	// 1. If gain the maintain side, then will use left outer join to gain its
	// related objects
	// but can use @OneToOne's fetch attribute to modify strategy
	@Test
	public void testOneToOneFind() {
		Department dept = entityManager.find(Department.class, 19);
		System.out.println(dept.getDeptName());
		System.out.println(dept.getMgr().getMgrName());
	}

	// For 1-1 relationship, it should save the side that does not maintain
	// (that does not have foreign key)
	// so it will not have more extra UPDATE SQL
	@Test
	public void testOneToOnePersistence() {

		Manager mgr = new Manager();
		mgr.setMgrName("M-BB");

		Department dept = new Department();
		dept.setDeptName("D-BB");

		mgr.setDept(dept);
		dept.setMgr(mgr);

		entityManager.persist(dept);
		entityManager.persist(mgr);

	}

	@Test
	public void testManyToOneUpdate() {

		Order order = entityManager.find(Order.class, 11);
		order.getCustomer().setLastName("FFF");
	}

	// Cannot directly delete "one" side, because of the foreign key
	@Test
	public void testManyToOneRemove() {
		// Order order = entityManager.find(Order.class, 10);
		// entityManager.remove(order);

		Customer customer = entityManager.find(Customer.class, 9);
		entityManager.remove(customer);

	}

	// In default, it uses outer join
	@Test
	public void testManyToOneFind() {
		Order order = entityManager.find(Order.class, 10);
		System.out.println(order.getOrderName());

		System.out.println(order.getCustomer().getLastName());

	}

	// For multi-direction 1-n relationship, persistent process will save "n"
	// side
	@Test
	public void testManyToOnePersist() {

		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBrith(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("yy@gmail.com");
		customer.setLastName("YY");

		Order order1 = new Order();
		order1.setOrderName("O-YY-1");

		Order order2 = new Order();
		order2.setOrderName("O-YY-2");

		customer.getOrders().add(order1);
		customer.getOrders().add(order2);

		order1.setCustomer(customer);
		order2.setCustomer(customer);

		entityManager.persist(customer);
		entityManager.persist(order1);
		entityManager.persist(order2);

	}

	@Test
	public void testFlush() {
		Customer customer = entityManager.find(Customer.class, 1);
		System.out.println(customer);

		customer.setLastName("AA");
		entityManager.flush();

	}

	// If input a detached object which means the object has OID
	// 1. If there is an corresponding object in EntityManager cache
	// 2. JPA will copy detached object into the object in EntityManager cache.
	// 3. Update the queried object.
	@Test
	public void testMerge4() {

		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBrith(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("dd@gmail.com");
		customer.setLastName("DD");

		customer.setId(5);

		Customer customer2 = entityManager.find(Customer.class, 5);
		entityManager.merge(customer);
		System.out.println(customer == customer2);

	}

	// If input a detached object which means the object has OID
	// 1. If there is not this object in EntityManager cache
	// 2. If there is an object record in data base
	// 3. JPA will query the corresponding object, and return a object of the
	// record, then put the detached object in it.
	// 4. Update the queried object
	@Test
	public void testMerge3() {

		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBrith(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("ee@gmail.com");
		customer.setLastName("EE");

		customer.setId(5);

		Customer customer2 = entityManager.merge(customer);
		System.out.println(customer == customer2);
		// System.out.println("Customer: "+customer.getId());
		// System.out.println("Customer2: "+customer2.getId());

	}

	// If input a detached object which means the object has OID
	// 1. If there is not this object in EntityManager cache
	// 2. If there is not this object record in data base
	// 3. JPA will create a new object, then copy the detached object to the new
	// object
	// 4. Insert the new object
	@Test
	public void testMerge2() {

		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBrith(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("dd@gmail.com");
		customer.setLastName("DD");

		customer.setId(100);

		Customer customer2 = entityManager.merge(customer);
		System.out.println("Customer: " + customer.getId());
		System.out.println("Customer2: " + customer2.getId());

	}

	/**
	 * Similar with Hibernate's Session "saveOrUpdate" method
	 * 
	 * 
	 */
	// 1. If input a temporal object
	// It will create a new Object and copy the temporal object in it, then
	// persistent the new object
	// So the new object has id, not the temporal one.
	@Test
	public void testMerge1() {
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBrith(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("cc@gmail.com");
		customer.setLastName("CC");

		Customer customer2 = entityManager.merge(customer);
		System.out.println("Customer: " + customer.getId());
		System.out.println("Customer2: " + customer2.getId());
	}

	// Similar with hibernate's delete method
	@Test
	public void testRemove() {

		// Customer customer = new Customer();
		// customer.setId(2);

		Customer customer = entityManager.find(Customer.class, 3);

		entityManager.remove(customer);

	}

	// Similar with hibernate's "save" method.
	// The different that compares with Hibernate's save method: if the entity
	// has "id", then throw an exception
	@Test
	public void testPersistence() {

		Customer customer = new Customer();
		customer.setAge(15);
		customer.setBrith(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("bb@gmail.com");
		customer.setLastName("BB");

		entityManager.persist(customer);
		System.out.println(customer.getInfo());

	}

	// Similar with hibernate's Session "load" method
	@Test
	public void testGetReference() {

		Customer customer = entityManager.getReference(Customer.class, 1);
		System.out.println(customer.getClass().getName());
		System.out.println(customer.getClass().getName());
		System.out.println("------------------------------------");
		System.out.println(customer);

	}

	// Similar with hibernate's Session "get" method
	@Test
	public void testFind() {

		Customer customer = entityManager.find(Customer.class, 1);
		System.out.println("------------------------------------");
		System.out.println(customer);

	}

}

package com.tony.jpa.helloworld;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Table(name = "JPA_CUSTOMER")
@Entity
public class Customer {

	private Integer id;
	private String lastName;
	private String email;
	private int age;

	private Date createdTime;
	private Date brith;

	private Set<Order> orders = new HashSet<>();

	
	//Map single 1-n relationship
	//If at the "one" side use @OneToMany's mappedBy attribute, then cannot use @JoinColumn attribute anymore
//	@JoinColumn(name="CUSTOMER_ID")
	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.REMOVE},mappedBy="customer")
	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	// @TableGenerator(name="ID_GENERATOR",
	// table="jpa_id_generators",pkColumnName="PK_NAME",pkColumnValue="CUSTOMER_ID",
	// valueColumnName="PK_VALUE", allocationSize=100)
	// @GeneratedValue(strategy=GenerationType.TABLE, generator="ID_GENERATOR")
	@GeneratedValue
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "LAST_NAME")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Temporal(TemporalType.DATE)
	public Date getBrith() {
		return brith;
	}

	public void setBrith(Date brith) {
		this.brith = brith;
	}

	// Tool method. It is not neccessary to be mapped
	@Transient
	public String getInfo() {

		return "lastName: " + lastName + ", email: " + email;

	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", lastName=" + lastName + ", email=" + email + ", age=" + age + ", createdTime="
				+ createdTime + ", brith=" + brith + "]";
	}

}

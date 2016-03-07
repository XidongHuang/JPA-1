package com.tony.jpa.helloworld;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Table(name="JPA_ITEMS")
@Entity
public class Item {

	private Integer id;
	private String itemName;
	
	private Set<Category> categories = new HashSet<>();

	@GeneratedValue
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="ITEM_NAME")
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	
	
	//Use @ManyToMany to map the relationship
	//Use @JoinTable to map the middle table
	//1. "name" points to middle table
	//2. "joinColumns" map the foreign key for the current class in middle table
	//2.1 "name" points foreign key column's name
	//2.2 "referencedColumnName" points to the which foreign key column will map to the current table's column 
	//3. "inverseJoinColumns" points to which foreign column in middle table for related class 
	@JoinTable(name="ITEM_CATEGORY",joinColumns={@JoinColumn(name="ITEM_ID", referencedColumnName="ID")},
			inverseJoinColumns={@JoinColumn(name="CATEGORY_ID", referencedColumnName="ID")})
	@ManyToMany
	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
	
	
}

package com.example.tagletagle.src.tag.entity;

import com.example.tagletagle.src.board.repository.SearchResultRepository;
import org.hibernate.annotations.DynamicInsert;

import com.example.tagletagle.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tag")
@NoArgsConstructor
@DynamicInsert
public class TagEntity extends BaseEntity implements SearchResultRepository {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Override
	public String getNickNameAndTagName(){
		return name != null ? name : null;
	}

	@Override
	public String getType(){
		return "user";
	}
}

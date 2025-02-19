package com.example.tagletagle.src.tag.entity;


import com.example.tagletagle.src.tag.dto.TagDTO;
import org.hibernate.annotations.DynamicInsert;

import com.example.tagletagle.base.BaseEntity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tag_request")
@NoArgsConstructor
@DynamicInsert
public class TagRequestEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column
	private String name;

	public TagRequestEntity(TagDTO tagDTO){
		this.name = tagDTO.getName();
	}

}

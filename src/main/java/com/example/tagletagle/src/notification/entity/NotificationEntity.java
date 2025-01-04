package com.example.tagletagle.src.notification.entity;

import org.hibernate.annotations.DynamicInsert;

import com.example.tagletagle.base.BaseEntity;
import com.example.tagletagle.config.NotificationType;
import com.example.tagletagle.src.board.entity.PostEntity;
import com.example.tagletagle.src.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "notification")
@NoArgsConstructor
@DynamicInsert
public class NotificationEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "notification_type", nullable = false)
	private NotificationType notificationType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "related_user_id")
	private UserEntity relatedUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "related_post_id")
	private PostEntity relatedPost;

	@Column(name = "is_read")
	private Boolean isRead = Boolean.FALSE;
}

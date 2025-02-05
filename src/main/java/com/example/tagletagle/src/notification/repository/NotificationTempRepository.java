package com.example.tagletagle.src.notification.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.tagletagle.config.NotificationType;
import com.example.tagletagle.src.notification.dto.NotificationInfoDTO;

@Repository
public class NotificationTempRepository {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public NotificationTempRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<Long> findFollowerIds(Long userId){

		String sql =
			"SELECT f.id AS id, " +
				"       f.follower_id AS followerId " +
				"FROM follows f " +
				"WHERE f.following_id = ?";

		List<Long> followerIdList = jdbcTemplate.query(sql,
			new RowMapper<Long>() {
				@Override
				public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
					Long followerId = rs.getLong("followerId");

					return followerId;
				}
			},
			userId

		);

		return followerIdList;
	}

	public List<NotificationInfoDTO> findNotificationInfoList(Long userId){

		String sql = "SELECT * from notification AS n " +
			"WHERE n.user_id = ?";

		List<NotificationInfoDTO> notificationInfoDTOList = jdbcTemplate.query(sql,
			new RowMapper<NotificationInfoDTO>() {
				@Override
				public NotificationInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
					NotificationType notificationType = NotificationType.valueOf(rs.getString("notification_type"));
					Long relatedUserId = rs.getLong("related_user_id");
					Long relatedPostId = rs.getLong("related_post_id");
					Boolean isRead = rs.getBoolean("is_read");

					return new NotificationInfoDTO(notificationType, relatedUserId, relatedPostId, isRead);
				}
			},
			userId

		);

		return notificationInfoDTOList;

	}


	// 게시글 저장 시 알림 생성
	public void insertNotificationsBySave(Long relatedUserId, Long relatedPostId, List<Long> followerIdList){

		String sql = "INSERT INTO notification (related_user_id, related_post_id, notification_type, user_id, is_read) " +
			"SELECT :relatedUserId, :relatedPostId, :notificationType, id, false FROM user WHERE id IN (:ids)";


		Map<String, Object> params = new HashMap<>();
		params.put("relatedUserId", relatedUserId);
		params.put("relatedPostId", relatedPostId);
		params.put("notificationType", "SAVE");
		params.put("ids", followerIdList);

		namedParameterJdbcTemplate.update(sql, params);


	}




}

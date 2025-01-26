package com.example.tagletagle.src.board.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.tagletagle.src.board.dto.CommentInfoDTO;
import com.example.tagletagle.src.board.dto.PostInfoDTO;

@Repository
public class BoardRepository {

	private final JdbcTemplate jdbcTemplate;

	public BoardRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<PostInfoDTO> findPostsByAuthorAndUser(Long authorId, Long userId) {

		String sql = "SELECT p.id AS postId, p.title AS title, p.url AS url, " +
			"p.comment_count AS commentCount, p.like_count AS likeCount, p.scrap_count AS scrapCount, " +
			"u.id AS authorId, u.nickname AS authorNickname, u.profile_img_url AS authorProfileImgUrl, " +
			"pl.id AS isLike, ps.id AS isScrap, " +
			"GROUP_CONCAT(DISTINCT t.id SEPARATOR ', ') AS tagIds, " +
			"GROUP_CONCAT(DISTINCT t.name SEPARATOR ', ') AS tagNames " +
			"FROM post p " +
			"INNER JOIN user u ON u.id = p.user_id " +
			"LEFT JOIN post_like pl ON pl.post_id = p.id AND pl.user_id = ? " +
			"LEFT JOIN post_scrap ps ON ps.post_id = p.id AND ps.user_id = ? " +
			"LEFT JOIN post_tag pt ON pt.post_id = p.id " +
			"LEFT JOIN tag t ON pt.tag_id = t.id " +
			"WHERE p.user_id = ? " +
			"GROUP BY p.id, p.title, p.url, p.comment_count, p.like_count, p.scrap_count, " +
			"u.id, u.nickname, u.profile_img_url, pl.id, ps.id";


		List<PostInfoDTO> postInfoDTOList = jdbcTemplate.query(sql,
			new RowMapper<PostInfoDTO>() {
				@Override
				public PostInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
					Long postId = rs.getLong("postId");
					String title = rs.getString("title");
					String url = rs.getString("title");
					Long commentCount = rs.getLong("commentCount");
					Long likeCount = rs.getLong("likeCount");
					Long scrapCount = rs.getLong("scrapCount");
					Long authorId = rs.getLong("authorId");
					String authorNickname = rs.getString("authorNickname");
					String authorProfileImgUrl = rs.getString("authorProfileImgUrl");
					Long isLike = rs.getLong("isLike");
					Long isScrap = rs.getLong("isScrap");
					String tagIds = rs.getString("tagIds");
					String tagNames = rs.getString("tagNames");

					System.out.println("isLike :" + isLike);
					System.out.println("isScrap :" + isScrap);

					return new PostInfoDTO(postId, title, url, commentCount, likeCount, scrapCount, authorId, authorNickname, authorProfileImgUrl, isLike, isScrap,tagIds, tagNames);
				}
			},
			userId, userId, authorId

		);
		return postInfoDTOList;


	}

	public List<PostInfoDTO> findPostsByAuthorAndUserWithTag(Long authorId, Long userId, Long tagId) {

		String sql = "SELECT p.id AS postId, p.title AS title, p.url AS url, " +
			"p.comment_count AS commentCount, p.like_count AS likeCount, p.scrap_count AS scrapCount, " +
			"u.id AS authorId, u.nickname AS authorNickname, u.profile_img_url AS authorProfileImgUrl, " +
			"pl.id AS isLike, ps.id AS isScrap, " +
			"GROUP_CONCAT(DISTINCT t.id SEPARATOR ', ') AS tagIds, " +
			"GROUP_CONCAT(DISTINCT t.name SEPARATOR ', ') AS tagNames " +
			"FROM post p " +
			"INNER JOIN user u ON u.id = p.user_id " +
			"LEFT JOIN post_like pl ON pl.post_id = p.id AND pl.user_id = ? " +
			"LEFT JOIN post_scrap ps ON ps.post_id = p.id AND ps.user_id = ? " +
			"LEFT JOIN post_tag pt ON pt.post_id = p.id " +
			"LEFT JOIN tag t ON pt.tag_id = t.id " +
			"WHERE p.user_id = ? AND t.id = ? " +
			"GROUP BY p.id, p.title, p.url, p.comment_count, p.like_count, p.scrap_count, " +
			"u.id, u.nickname, u.profile_img_url, pl.id, ps.id";


		List<PostInfoDTO> postInfoDTOList = jdbcTemplate.query(sql,
			new RowMapper<PostInfoDTO>() {
				@Override
				public PostInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
					Long postId = rs.getLong("postId");
					String title = rs.getString("title");
					String url = rs.getString("title");
					Long commentCount = rs.getLong("commentCount");
					Long likeCount = rs.getLong("likeCount");
					Long scrapCount = rs.getLong("scrapCount");
					Long authorId = rs.getLong("authorId");
					String authorNickname = rs.getString("authorNickname");
					String authorProfileImgUrl = rs.getString("authorProfileImgUrl");
					Long isLike = rs.getLong("isLike");
					Long isScrap = rs.getLong("isScrap");
					String tagIds = rs.getString("tagIds");
					String tagNames = rs.getString("tagNames");

					return new PostInfoDTO(postId, title, url, commentCount, likeCount, scrapCount, authorId, authorNickname, authorProfileImgUrl, isLike, isScrap,tagIds, tagNames);
				}
			},
			userId, userId, authorId, tagId

		);
		return postInfoDTOList;


	}

	public List<CommentInfoDTO> findCommentsByPost(Long postId) {

		String sql =
			"SELECT c.id AS commentId, " +
				"       c.contents AS contents, " +
				"       u.id AS authorId, " +
				"       u.nickname AS authorNickname, " +
				"       u.profile_img_url AS authorProfileImgUrl " +
				"FROM comment c " +
				"INNER JOIN user u ON c.user_id = u.id " +
				"WHERE c.post_id = ?";



		List<CommentInfoDTO> commentInfoDTOList = jdbcTemplate.query(sql,
			new RowMapper<CommentInfoDTO>() {
				@Override
				public CommentInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
					Long commentId = rs.getLong("commentId");
					String contents = rs.getString("contents");
					Long authorId = rs.getLong("authorId");
					String authorNickname = rs.getString("authorNickname");
					String authorProfileImgUrl = rs.getString("authorProfileImgUrl");

					return new CommentInfoDTO(commentId, contents, authorId, authorNickname, authorProfileImgUrl);
				}
			},
			postId

		);
		return commentInfoDTOList;


	}




}

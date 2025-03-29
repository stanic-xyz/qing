package cn.chenyunlong.qing.domain.anime.anime.exception;

// 评论相关异常
public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String commentId) { super("评论不存在: " + commentId); }
}

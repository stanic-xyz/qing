package cn.chenyunlong.qing.auth.domain.user.exception;

// 评论相关异常
public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String commentId) {
        super("评论不存在: " + commentId);
    }
}


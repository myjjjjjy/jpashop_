package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException{

    // 메세지 넘겨서 예외처리 할 때의 근원적 예외처리 위해서 생성자 다 만들어주기.
    public NotEnoughStockException() {
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
    
}

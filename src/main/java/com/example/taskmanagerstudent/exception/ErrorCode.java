package com.example.taskmanagerstudent.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNWANTEDEXCEPTION(500, "Lỗi không mong muốn", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED_EXCEPTION(9999, "UNCATEGORIZED ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    NO_CATEGORY_FOUND(1000, "Không có danh sách category nào", HttpStatus.NOT_FOUND),
    INVALID_KEY(1001, "INVALID MESSAGE", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1002, "Không tìm thấy Category nào", HttpStatus.NOT_FOUND),
    CATEGORY_EXISTED(1003, "Mã danh mục không được trùng", HttpStatus.NOT_FOUND),
    CATEGORY_DELETE(1004, "Xóa thất bại", HttpStatus.BAD_REQUEST),
    PRODUCT_DELETE(1005, "Xóa PRODUCT thất bại", HttpStatus.BAD_REQUEST),
    RUNTIME_ERROR(1006, "Lỗi do người dùng nhập chưa đúng dữ liệu, hoặc dữ liệu xung đột", HttpStatus.BAD_REQUEST),
    SQL_ERROR(1007, "Lỗi trong quá trình đưa dữ liệu và cơ sở dữ liệu", HttpStatus.BAD_REQUEST),
    ;
    private Integer code;
    private String message;
    private HttpStatus status;

    ErrorCode(Integer code, String message, HttpStatus status) {
        this.code = code;
        this.status = status;
        this.message = message;
    }


}

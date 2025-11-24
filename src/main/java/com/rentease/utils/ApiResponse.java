package com.rentease.utils;

public class ApiResponse<T> {
     private String timestamp;
    private int status;
    private String message;
    private String path;
    private T data;

    public ApiResponse(int status, String message, String path, T data) {
        this.setTimestamp(java.time.OffsetDateTime.now().toString());
        this.setStatus(status);
        this.setMessage(message);
        this.setPath(path);
        this.setData(data);
    }

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

    // Getters and setters ...
}

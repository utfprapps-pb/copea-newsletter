package br.edu.utfpr.reponses;

public class GenericResponse<T> {
    private T data;

    public static <T> GenericResponse<T> of(T data) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setData(data);
        return response;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}

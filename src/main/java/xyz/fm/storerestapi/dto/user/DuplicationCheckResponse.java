package xyz.fm.storerestapi.dto.user;

public class DuplicationCheckResponse {

    private Boolean exist;

    public DuplicationCheckResponse(Boolean exist) {
        this.exist = exist;
    }

    public Boolean getExist() {
        return exist;
    }
}

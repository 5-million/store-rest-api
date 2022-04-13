package xyz.fm.storerestapi.dto.user.vendor;

import java.util.ArrayList;
import java.util.List;

public class VendorManagerApproveResult {

    private ApproveResult<ApproveSuccess> success;
    private ApproveResult<ApproveFail> fail;

    public VendorManagerApproveResult(List<ApproveSuccess> success, List<ApproveFail> fail) {
        this.success = new ApproveResult<>(success);
        this.fail = new ApproveResult<>(fail);
    }

    public ApproveResult<ApproveSuccess> getSuccess() {
        return success;
    }

    public ApproveResult<ApproveFail> getFail() {
        return fail;
    }

    public static class ApproveResult<T> {
        private Integer size;
        private List<T> list;

        public ApproveResult(List<T> list) {
            this.size = list.size();
            this.list = new ArrayList<>(list);
        }

        public Integer getSize() {
            return size;
        }

        public List<T> getList() {
            return list;
        }
    }

    public static class ApproveSuccess {
        private String target;

        public ApproveSuccess(String target) {
            this.target = target;
        }

        public String getTarget() {
            return target;
        }
    }

    public static class ApproveFail {
        public static final String ALREADY_APPROVED = "이미 승인된 매니저입니다.";
        public static final String NOT_SAME_VENDOR = "당사 매니저가 아닙니다.";

        private String target;
        private String cause;

        public ApproveFail(String target, String cause) {
            this.target = target;
            this.cause = cause;
        }

        public String getTarget() {
            return target;
        }

        public String getCause() {
            return cause;
        }
    }
}

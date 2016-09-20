package xin.nic.sdk.registrar.module;

/**
 * 类AuditEnum.java的实现描述：域名/联系人 审核枚举类
 * 
 * @author shiming.zhao 2015年10月8日 下午4:06:26
 */
public enum AuditEnum implements IntEnum<AuditEnum> {

    REALNAME_VERIFICATION_UNCOMPLETED(0, "未实名验证"),
    PENDING_VERIFICATION(1, "实名验证中"),
    REALNAME_VERIFICATION_COMPLETED(2, "实名验证成功");

    private int    code;

    private String desc;

    private AuditEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

}

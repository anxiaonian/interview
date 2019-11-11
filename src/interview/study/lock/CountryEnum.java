package interview.study.lock;

/**
 * @Auther: xujh
 * @Date: 2019/11/9 23:29
 * @Description:
 */
public enum CountryEnum {

    ONE(1,"齐"),TWO(2,"楚"),THREE(3,"燕"),FOUR(4,"韩"),FIVE(5,"赵"),SIX(6,"魏");

    private Integer retCode;
    private String retMessage;

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetMessage(String retMessage) {
        this.retMessage = retMessage;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    CountryEnum(Integer retCode, String retMessage) {
        this.retCode = retCode;
        this.retMessage = retMessage;
    }

    public static CountryEnum getCountryEnum(int index){
        CountryEnum[] enums = CountryEnum.values();
        for (CountryEnum element : enums) {
            if(index == element.getRetCode()){
                return element;
            }
        }
        return null;
    }
}

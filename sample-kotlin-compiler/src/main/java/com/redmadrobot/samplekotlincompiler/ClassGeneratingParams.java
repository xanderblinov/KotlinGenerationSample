package com.redmadrobot.samplekotlincompiler;

/**
 * Date: 12-Jan-16 Time: 15:09
 *
 * @author Alexander Blinov
 */
final class ClassGeneratingParams {

    static final String KOTLIN_FILE_EXTENSION = ".kt";

    private String mName;

    private String mBody;

    private String mPackage;

    public ClassGeneratingParams() {
    }

    public void setName(String name) {
        mName = name;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public String getName() {
        return mName;
    }

    public String getBody() {
        return mBody;
    }

    public String getPackage() {
        return mPackage;
    }

    public void setPackage(final String aPackage) {
        mPackage = aPackage;
    }

    public String getSimpleFileName() {
        return mName + KOTLIN_FILE_EXTENSION;
    }
}

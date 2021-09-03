package edu.bmv.studentorder.domain;

public enum StudentOrderStatus {
    START,CHECKED;

    public static StudentOrderStatus fromValue(int value){

        for (StudentOrderStatus st : StudentOrderStatus.values()){
            if(st.ordinal() == value){
                return st;
            }
        }
        throw new RuntimeException("Unknown value: " + value);
    }
}

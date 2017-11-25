package controller;

public class IDGenerator  {
    private static long ID=0;


    public static long getNextID() {


        try{
            if (ID > 9999) throw new IDOverFlowException( "Maximum number of Songs was reached !" );

            return ID++;
        } catch (IDOverFlowException e ) {
           e.printStackTrace();
        }

        return -1;
    }

}

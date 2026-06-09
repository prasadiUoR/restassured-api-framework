package endpoints;

public class Routes {

    public static String auth(){
        return "/auth";
    }

    public static String createBooking(){
        return "/booking";
    }

    public static String getBooking(int id){
        return "/booking/" + id;
    }

    public static String updateBooking(int id){
        return "/booking/" + id;
    }

    public static String deleteBooking(int id){
        return "/booking/" + id;
    }

}

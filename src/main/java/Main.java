import org.example.config.Config;
import org.example.controller.AuthController;
import org.example.db.DataBase;
import org.example.db.InitDataBase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);

        AuthController authController = (AuthController) applicationContext.getBean("authController");
        authController.start();

        InitDataBase initDataBase = (InitDataBase) applicationContext.getBean("initDataBase");
        initDataBase.adminInit();
        initDataBase.addCompanyCard();

        DataBase dataBase= (DataBase) applicationContext.getBean("dataBase");
        dataBase.initDatabase();
    }
}

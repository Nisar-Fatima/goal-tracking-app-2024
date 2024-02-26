package com.techtide.goaltracking;
import com.techtide.goaltracking.config.StageManager;
import com.techtide.goaltracking.enums.FxmlView;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JAVAFXApplication extends Application {
    protected ConfigurableApplicationContext springContext;
    protected StageManager stageManager;

    public void init()  {
        springContext = springBootApplicationContext();
    }

    @Override
    public void start(Stage stage) {
        stageManager = springContext.getBean(StageManager.class, stage);
        displayInitialScene();

    }
    public void stop()  {
        springContext.close();

    }
    protected void displayInitialScene(){
        stageManager.switchScene(FxmlView.LOGIN);

    }
    private ConfigurableApplicationContext springBootApplicationContext(){
        final SpringApplicationBuilder builder = new SpringApplicationBuilder(GoaltrackingApplication.class);
        builder.headless(false);
        final String[] args = getParameters().getRaw().toArray(String[] :: new);
        return builder.run(args);
    }
}


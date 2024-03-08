package com.techtide.goaltracking.enums;
import java.util.ResourceBundle;
public enum FxmlView {
    LOGIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/login.fxml";
        }
    },
    SIGNUP {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("signUp.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/signUp.fxml";
        }
    },
    MENU {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("menu.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/menu.fxml";
        }
    },
    NEWGOAL {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("newGoal.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/newGoalPage.fxml";
        }
    },
    CURRENTGOAL{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("currentGoal.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/currentGoalPage.fxml";
        }
    },
        //    STOPWATCH{
//        @Override
//        public String getTitle() {
//            return getStringFromResourceBundle("stopWatch.title");
//        }
//
//        @Override
//        public String getFxmlFile() {
//            return "/fxml/stopWatch.fxml";
//        }
//    },
    RECORDS{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("record.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/record.fxml";
        }
    };
//    SPREADSHEET{
//        @Override
//        public String getTitle() {
//            return getStringFromResourceBundle("spreadsheet.title");
//        }
//
//        @Override
//        public String getFxmlFile() {
//            return "/fxml/spreadsheet.fxml";
//        }
//    },
//    TASK{
//        @Override
//        public String getTitle() {
//            return getStringFromResourceBundle("task.title");
//        }
//
//        @Override
//        public String getFxmlFile() {
//            return "/fxml/task.fxml";
//        }
//    },
//    CONTACTUS{
//        @Override
//        public String getTitle() {
//            return getStringFromResourceBundle("contactUs.title");
//        }
//        @Override
//        public String getFxmlFile() {
//            return "/fxml/contactUs.fxml";
//        }
//    };
        static String getStringFromResourceBundle(final String key) {
            return ResourceBundle.getBundle("Bundle").getString(key);
        }

        public abstract String getTitle();

        public abstract String getFxmlFile();

    }


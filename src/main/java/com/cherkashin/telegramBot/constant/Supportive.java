package com.cherkashin.telegramBot.constant;

public enum Supportive {
    ;

    public static class Commands {

        public static class CallbackQuery {
            public static final String REQUEST_NEXT_LIST_VACANCIES = "След. лист";
            public static final String REQUEST_PREVIOUS_LIST_VACANCIES = "Пред. лист";

            public static final String CONTINUE_LIST_TECHNOLOGIES = "След. Страница";
            public static final String PREVIOUS_LIST_TECHNOLOGIES = "Предыдущая Страница";
            public static final String ITEM_TECHNOLOGIES = "TECH_ITEM_";
        }

        public static final String VACANCIES_GET_ALL = "📋 Получить все вакансии";
        public static final String SUBSCRIBE_ON_VACANCIES = "🔔 Включить уведомления";
        public static final String UNSUBSCRIBE_VACANCIES = "🔕 Отключить уведомления";
        public static final String SETTING_NOTIFICATION = "Настроить уведомления";
        public static final String BACK_TO_HOME = "Вернуться в главное меню";
    }

    public static class Notification {
        public final static String ADD_NEW_VACANCY = "Новая вакансия";
        public final static String CLOSE_VACANCY = "Вакансия закрылась";
    }

}

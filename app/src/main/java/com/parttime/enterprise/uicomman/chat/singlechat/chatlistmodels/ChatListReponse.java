package com.parttime.enterprise.uicomman.chat.singlechat.chatlistmodels;

import java.io.Serializable;
import java.util.List;

public class ChatListReponse implements Serializable {


    /**
     * code : 200
     * message : Success.
     * result : {"new_user":[{"user_id":53,"first_name":"Russell","gender":"men","date_teamup_time":"2019-03-15T11:44:24.000Z","user_image":"http://52.27.53.102/blixr/public/users/e7ad521fc195b7093b9fd9202e0ab24b.jpeg"}],"old_user":[{"user_id":54,"first_name":"John","gender":"","date_teamup_time":"2019-03-18T14:13:46.000Z","user_image":"http://52.27.53.102/blixr/public/users/user17.jpeg","message":"","sender_id":"","message_created_at":"","unread_count":""}],"one_sided_date_up":[{"user_id":58,"first_name":"Deep Verma","gender":"","date_teamup_time":"2019-03-15T11:16:21.000Z","user_image":"http://52.27.53.102/blixr/public/users/8195e80fe0417ce8072fcb7991fe09e8.jpeg","message":"","sender_id":"","message_created_at":"","unread_count":""},{"user_id":52,"first_name":"Nyra","gender":"men","date_teamup_time":"2019-03-18T20:18:00.000Z","user_image":"http://52.27.53.102/blixr/public/users/user8.jpeg","message":"","sender_id":"","message_created_at":"","unread_count":""},{"user_id":50,"first_name":"Mayra","gender":"men","date_teamup_time":"2019-03-19T07:44:09.000Z","user_image":"http://52.27.53.102/blixr/public/users/f5ed08513755e5bdd85e47ee5dfaf9dc.jpeg","message":"","sender_id":"","message_created_at":"","unread_count":""}],"like_count":0}
     */

    private int code;
    private String message;
    private ResultBean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * new_user : [{"user_id":53,"first_name":"Russell","gender":"men","date_teamup_time":"2019-03-15T11:44:24.000Z","user_image":"http://52.27.53.102/blixr/public/users/e7ad521fc195b7093b9fd9202e0ab24b.jpeg"}]
         * old_user : [{"user_id":54,"first_name":"John","gender":"","date_teamup_time":"2019-03-18T14:13:46.000Z","user_image":"http://52.27.53.102/blixr/public/users/user17.jpeg","message":"","sender_id":"","message_created_at":"","unread_count":""}]
         * one_sided_date_up : [{"user_id":58,"first_name":"Deep Verma","gender":"","date_teamup_time":"2019-03-15T11:16:21.000Z","user_image":"http://52.27.53.102/blixr/public/users/8195e80fe0417ce8072fcb7991fe09e8.jpeg","message":"","sender_id":"","message_created_at":"","unread_count":""},{"user_id":52,"first_name":"Nyra","gender":"men","date_teamup_time":"2019-03-18T20:18:00.000Z","user_image":"http://52.27.53.102/blixr/public/users/user8.jpeg","message":"","sender_id":"","message_created_at":"","unread_count":""},{"user_id":50,"first_name":"Mayra","gender":"men","date_teamup_time":"2019-03-19T07:44:09.000Z","user_image":"http://52.27.53.102/blixr/public/users/f5ed08513755e5bdd85e47ee5dfaf9dc.jpeg","message":"","sender_id":"","message_created_at":"","unread_count":""}]
         * like_count : 0
         */

        private int like_count;
        private List<NewUserBean> new_user;
        private List<OldUserBean> old_user;
        private List<OneSidedDateUpBean> one_sided_date_up;

        public int getLike_count() {
            return like_count;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public List<NewUserBean> getNew_user() {
            return new_user;
        }

        public void setNew_user(List<NewUserBean> new_user) {
            this.new_user = new_user;
        }

        public List<OldUserBean> getOld_user() {
            return old_user;
        }

        public void setOld_user(List<OldUserBean> old_user) {
            this.old_user = old_user;
        }

        public List<OneSidedDateUpBean> getOne_sided_date_up() {
            return one_sided_date_up;
        }

        public void setOne_sided_date_up(List<OneSidedDateUpBean> one_sided_date_up) {
            this.one_sided_date_up = one_sided_date_up;
        }

        public static class NewUserBean implements Serializable {
            /**
             * user_id : 53
             * first_name : Russell
             * gender : men
             * date_teamup_time : 2019-03-15T11:44:24.000Z
             * user_image : http://52.27.53.102/blixr/public/users/e7ad521fc195b7093b9fd9202e0ab24b.jpeg
             */

            private int user_id;
            private String first_name;
            private String gender;
            private String date_teamup_time;
            private String user_image;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getFirst_name() {
                return first_name;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getDate_teamup_time() {
                return date_teamup_time;
            }

            public void setDate_teamup_time(String date_teamup_time) {
                this.date_teamup_time = date_teamup_time;
            }

            public String getUser_image() {
                return user_image;
            }

            public void setUser_image(String user_image) {
                this.user_image = user_image;
            }
        }

        public static class OldUserBean implements Serializable {
            /**
             * user_id : 54
             * first_name : John
             * gender :
             * date_teamup_time : 2019-03-18T14:13:46.000Z
             * user_image : http://52.27.53.102/blixr/public/users/user17.jpeg
             * message :
             * sender_id :
             * message_created_at :
             * unread_count :
             */

            private int user_id;
            private String first_name;
            private String gender;
            private String date_teamup_time;
            private String user_image;
            private String message;
            private String sender_id;
            private String message_created_at;
            private String unread_count;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getFirst_name() {
                return first_name;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getDate_teamup_time() {
                return date_teamup_time;
            }

            public void setDate_teamup_time(String date_teamup_time) {
                this.date_teamup_time = date_teamup_time;
            }

            public String getUser_image() {
                return user_image;
            }

            public void setUser_image(String user_image) {
                this.user_image = user_image;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getSender_id() {
                return sender_id;
            }

            public void setSender_id(String sender_id) {
                this.sender_id = sender_id;
            }

            public String getMessage_created_at() {
                return message_created_at;
            }

            public void setMessage_created_at(String message_created_at) {
                this.message_created_at = message_created_at;
            }

            public String getUnread_count() {
                return unread_count;
            }

            public void setUnread_count(String unread_count) {
                this.unread_count = unread_count;
            }
        }

        public static class OneSidedDateUpBean implements Serializable {
            /**
             * user_id : 58
             * first_name : Deep Verma
             * gender :
             * date_teamup_time : 2019-03-15T11:16:21.000Z
             * user_image : http://52.27.53.102/blixr/public/users/8195e80fe0417ce8072fcb7991fe09e8.jpeg
             * message :
             * sender_id :
             * message_created_at :
             * unread_count :
             */

            private int user_id;
            private String first_name;
            private String gender;
            private String date_teamup_time;
            private String user_image;
            private String message;
            private String sender_id;
            private String message_created_at;
            private String unread_count;
            private String age;

            public String getAge() {
                return age;
            }

            public void setAge(String age) {
                this.age = age;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getFirst_name() {
                return first_name;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getDate_teamup_time() {
                return date_teamup_time;
            }

            public void setDate_teamup_time(String date_teamup_time) {
                this.date_teamup_time = date_teamup_time;
            }

            public String getUser_image() {
                return user_image;
            }

            public void setUser_image(String user_image) {
                this.user_image = user_image;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getSender_id() {
                return sender_id;
            }

            public void setSender_id(String sender_id) {
                this.sender_id = sender_id;
            }

            public String getMessage_created_at() {
                return message_created_at;
            }

            public void setMessage_created_at(String message_created_at) {
                this.message_created_at = message_created_at;
            }

            public String getUnread_count() {
                return unread_count;
            }

            public void setUnread_count(String unread_count) {
                this.unread_count = unread_count;
            }
        }
    }
}

package com.parttime.enterprise.uicomman.chat.singlechat.chatlistmodels;

import java.io.Serializable;
import java.util.List;

public class ChatMessage implements Serializable {

    private List<ChatData> chat;

    public List<ChatData> getChat() {
        return chat;
    }

    public void setChat(List<ChatData> chat) {
        this.chat = chat;
    }

    public static class ChatData implements Serializable {


        /*
            created_at: "2019-08-08T00:00:00.000Z",
            message: "Ok",
            read_status: "0",
            receiver_id: 4,
            receiver_type: "enterprise",
            sender_id: 27,
            sender_type: "user"
            */

        private String created_at;
        private String userImage;
        private String message;
        private String read_status;
        private int receiver_id;
        private String receiver_type;
        private int sender_id;
        private String sender_type;
        private String message_type;
        private String end_time;
        private String start_time;
        private String date;
        private String imgReceiver;
        private String nameReceiver;

        public String getImgReceiver() {
            return imgReceiver;
        }

        public void setImgReceiver(String imgReceiver) {
            this.imgReceiver = imgReceiver;
        }

        public String getNameReceiver() {
            return nameReceiver;
        }

        public void setNameReceiver(String nameReceiver) {
            this.nameReceiver = nameReceiver;
        }

        public String getMessage_type() {
            return message_type;
        }

        public void setMessage_type(String message_type) {
            this.message_type = message_type;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRead_status() {
            return read_status;
        }

        public void setRead_status(String read_status) {
            this.read_status = read_status;
        }

        public int getReceiver_id() {
            return receiver_id;
        }

        public void setReceiver_id(int receiver_id) {
            this.receiver_id = receiver_id;
        }

        public String getReceiver_type() {
            return receiver_type;
        }

        public void setReceiver_type(String receiver_type) {
            this.receiver_type = receiver_type;
        }

        public int getSender_id() {
            return sender_id;
        }

        public void setSender_id(int sender_id) {
            this.sender_id = sender_id;
        }

        public String getSender_type() {
            return sender_type;
        }

        public void setSender_type(String sender_type) {
            this.sender_type = sender_type;
        }
    }
}

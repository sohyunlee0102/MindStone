# MindStone Backend (develop branch)
![image](https://github.com/user-attachments/assets/4be5e5a9-5be9-4469-90c4-20b8b968d79d)

### 🧠 Mental Care Service with Your Own Stone
A service designed to help users manage their emotions and habits by growing a stone that reflects their feelings. It also generates a daily journal automatically by tracking emotions and activities.

## Service Description

### 🪨 **Target Users**
- Fans of character management games
- People who don’t have time to write daily journals
- Individuals aiming to build habits

### 🪨 **Key Features**

1. **Character Raising**  
   Customize your stone by choosing an emotion (happiness, anger, sadness, etc.) and adjust its intensity. Manage your stone based on these emotions.
   
2. **Emotion Management**  
   When the stone is upset, the app suggests stress management techniques that you've previously used or that other users recommend. You can track the time spent on each activity.
   
3. **Emotion & Activity Stats**  
   Provides easy-to-read statistics on your emotional and habitual progress over the week, month, etc.

4. **Emotion & Habit Calendar**  
   Tracks your emotional and habitual progress with a calendar view, making it easier to see trends.

5. **Automatic Journal**  
   Generates a daily journal from the activities and emotional states you tracked throughout the day. You can add photos and modify the content.

## 🗒️ Database Design

[View ERD Diagram](https://www.erdcloud.com/d/dCAAyNhQZ6bnGAG3e)
![image](https://github.com/user-attachments/assets/b194619c-4f2d-4d82-8a38-8384aec8fd46)


## API Documentation

[Swagger API Documentation](http://15.165.241.217:8080/swagger-ui/index.html)

## What I Did

- **Initial Setup & Architecture**  
   Set up the project, implemented custom error handlers, and designed consistent response logic.
   
- **User Authentication**  
   Implemented sign-up, login, and JWT token management using Spring Security.

- **Email Services**  
   Enabled email-based verification and temporary password recovery through the SMTP protocol.

- **User Onboarding**  
   Implemented a guided onboarding process for first-time users.

- **Profile Management**  
   Created functionalities for profile editing, logout, and account deletion in the “My Page” section.

- **Habit Tracking**  
   Implemented habit tracking, notifications, and modification features in “My Page.”

- **Push Notifications**  
   Developed habit reminders that notify users at their preferred times.

- **Data Security**  
   Created a blacklist to store tokens for users who have logged out or deleted their accounts to prevent token theft.

- **Automated User Deletion**  
   Used Spring Scheduler to automatically delete permanently deleted users and expired tokens from the blacklist.

## Learned

1. **Collaboration Skills**  
   Gained experience collaborating with non-technical teams (PMs, designers, front-end developers). I learned how to write API documentation that is easy for front-end developers to understand using Swagger.

2. **MVC Pattern & Data Flow**  
   Honed my ability to design clean, efficient code by adhering to the MVC pattern and separating concerns between the layers.

3. **Git Flow Collaboration**  
   Improved my knowledge of Git Flow, enhancing my understanding of branch strategies for team collaboration.

4. **Code Readability**  
   Focused on writing clear and maintainable code so that team members could easily understand and build on it.

5. **Security Practices**  
   Strengthened my ability to design secure systems by using Spring Security for role-based access control, token management, and CORS configuration.

## 🚀 Technology Stack

- **Used Stacks**: Spring Boot, Spring Security, Spring Data JPA, MySQL
- **Used Tools**: Git, IntelliJ, EC2, RDS, Figma, Github Actions
- **Used Collaborations**: Notion, Github, Discord
   
## Implementaion Images

![image](https://github.com/user-attachments/assets/88b8632f-a20c-4735-9159-92576ecc49f0)
![image](https://github.com/user-attachments/assets/13b0de6f-ff28-4390-b565-8c5698afdcfd)
![image](https://github.com/user-attachments/assets/881aaf40-f75d-45f7-9552-9414bda93351)
![image](https://github.com/user-attachments/assets/2084058a-005c-4a76-a071-0550eec990a5)
![image](https://github.com/user-attachments/assets/596607c0-11ac-47db-8672-6f177bd9612f)
![image](https://github.com/user-attachments/assets/a4845dea-0b2b-4be0-b8ef-f2fb1b0f0716)
![image](https://github.com/user-attachments/assets/a21657f3-228f-435f-9b66-f2ec0d6b50d9)
![image](https://github.com/user-attachments/assets/557647d6-334d-4bca-9959-4b5154a46f63)
![image](https://github.com/user-attachments/assets/e5a3d76f-81da-49c2-96b0-ea6bd227f66e)
![image](https://github.com/user-attachments/assets/ec37dd74-4c24-42ff-8877-274f6d3ce94c)

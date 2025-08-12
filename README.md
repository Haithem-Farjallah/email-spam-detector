# 📧 Spam Detection System

This project is a **Spam Detection System** designed to identify spam emails using an AI model, with automated daily reporting in Excel format.

## 🔹 How It Works
1. **Email Extraction** – A Spring Boot service runs on a scheduled task to fetch all unread emails from the configured inbox.  
2. **AI Spam Detection** – Each email is sent to a Flask server that calls an AI model trained on a large dataset to determine whether the email is spam or not.  
   - **Why Flask?**  
     Flask is used to host the AI model because it offers a lightweight and flexible Python-based API layer, making it easy to integrate Python machine learning libraries (such as scikit-learn, TensorFlow, etc.) directly with our Java Spring Boot backend. This separation also keeps the AI environment independent from the Java service.  
3. **Database Storage** – The results are stored in a database for historical tracking and analytics.  
4. **Excel Report Generation** – At the end of each day, the system automatically generates an Excel report summarizing the detection results.

## ⚙️ Getting Started
1. Open the `application.properties` file.  
2. Update it with:  
   - Your **email credentials** (to fetch unread mails).  
   - Your **database credentials** (for result storage).  
3. Run the project – it will start detecting spam and generating reports automatically.

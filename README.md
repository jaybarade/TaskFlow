# 📋 TaskFlow - Task Management App

TaskFlow is a simple and efficient **Task Management Android Application** built using **Kotlin** and **Room Database**.  
It allows users to **add**, **update**, **delete**, and **mark tasks as completed or incomplete** with a clean and user-friendly interface.

---

## 🚀 Features

- ✅ Add new tasks
- ✏️ Update existing tasks
- ❌ Delete tasks
- 🔄 Change task status from **Incomplete → Complete**
- 📂 Separate task categories:
  - Incomplete Tasks
  - Completed Tasks
- 📅 Date Picker for task deadline/date selection
- 💾 Local data storage using **Room Database**
- 🎨 Simple and clean UI design

---

## 📱 Screens Included

- Splash Screen
- Home Screen
- Add Task Dialog
- Date Picker
- Task List Screen
- Update Task Dialog
- Completed Task Screen

---

## 🛠️ Tech Stack

### Language
- **Kotlin**

### Architecture / Components
- **RecyclerView**
- **Room Database**
- **Dialog / AlertDialog**
- **Material UI Components**
- **DatePickerDialog**
- **CardView**
- **Floating Action Button (FAB)**

---

## 🗂️ Project Structure

```bash
TaskFlow/
│── app/
│   ├── java/com/yourpackage/taskflow/
│   │   ├── database/
│   │   │   ├── TaskDatabase.kt
│   │   │   ├── TaskDao.kt
│   │   │   └── TaskEntity.kt
│   │   ├── adapter/
│   │   │   └── TaskAdapter.kt
│   │   ├── ui/
│   │   │   ├── MainActivity.kt
│   │   │   ├── AddTaskDialog.kt
│   │   │   └── UpdateTaskDialog.kt
│   │   └── model/
│   │       └── TaskModel.kt
│   └── res/
│       ├── layout/
│       ├── drawable/
│       ├── values/
│       └── mipmap/

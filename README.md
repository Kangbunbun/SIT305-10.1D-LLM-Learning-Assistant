# SIT305 Task 10.1D - LLM-Enhanced Learning Assistant App

## Overview

This project is an improved version of the LLM-Enhanced Learning Assistant App developed for SIT305 Task 10.1D. The app helps students learn through interest-based quiz tasks, AI-generated hints, AI answer explanations, learning history, profile sharing, and account upgrade features.

The Task 10.1D upgrade adds three new screens:

- Profile
- Learning History
- Upgrade Account

It also implements the required History, Sharing, and Purchasing features.

## Main Features

### 1. Account Setup and Interests

Users can sign up or log in using a username and email. During setup, users select their learning interests. These interests are used to recommend learning tasks on the Home screen.

### 2. Learning Tasks

The app displays quiz-style learning tasks based on the selected interests. Each task contains multiple-choice questions. After completing a task, users can submit their answers and view their score, selected answers, correct answers, and correct/incorrect status.

### 3. LLM-Powered Get Hint

While answering quiz questions, users can request an AI-generated hint. The app sends a structured prompt to the backend, which calls the Groq LLM API. The prompt and AI response are displayed in the UI.

### 4. LLM-Powered Explain My Answer

After submitting a task, users can request an AI explanation for each answer. The app explains why the correct answer is correct and why the user’s selected answer is correct or incorrect.

### 5. Learning History

The Learning History screen stores question-based learning records. Each history item shows:

- Topic
- Question
- Correct / Incorrect status
- Answered timestamp

Each card can be expanded to show:

- User answer
- Correct answer
- AI hint, if requested
- AI explanation, if requested
- Hint and explanation timestamps

If the user did not request a hint or explanation, the app displays “Not requested”.

### 6. Profile

The Profile screen shows:

- Username
- Email
- Current account plan
- Selected interests
- Total questions answered
- Correct answers
- Incorrect answers

Learning statistics are updated when the user submits quiz tasks.

### 7. Share Profile

The app uses Android’s native Share Intent to share a public profile summary. The shared content includes username, email, current plan, selected interests, and learning progress summary. Users can share this through supported apps such as Gmail, Messages, Drive, or other platforms.

### 8. Upgrade Account with Stripe Test Payment

The Upgrade Account screen includes three plans:

- Starter
- Intermediate
- Advanced

Starter is the default free plan. Intermediate and Advanced use Stripe Test Mode. The Android app requests a PaymentIntent from the Node.js backend. After successful test payment through Stripe PaymentSheet, the app updates the user’s current plan and displays it in the Profile screen.

## Tech Stack

### Android App

- Kotlin
- XML layouts
- ViewBinding
- Fragment-based UI
- Navigation Component
- RecyclerView
- SharedPreferences
- Retrofit
- Stripe Android SDK

### Backend

- Node.js
- Express
- Groq SDK
- Stripe SDK
- dotenv
- CORS

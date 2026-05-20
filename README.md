# \# SIT305 Task 10.1D - LLM Learning Assistant App

# 

# \## Overview

# 

# This is an Android learning assistant app for SIT305 Task 10.1D.

# 

# The app helps students practise learning tasks, get AI hints, review AI explanations, view learning history, share their profile, and upgrade their account using Stripe test payment.

# 

# \## Features

# 

# \- Sign up and log in

# \- Select learning interests

# \- Complete quiz-style learning tasks

# \- Get AI hints for questions

# \- Get AI explanations after answering

# \- View expandable learning history

# \- View profile and learning progress

# \- Share profile using Android Share Sheet

# \- Upgrade account using Stripe Test Mode

# 

# \## Project Structure

# 

# ```text

# 

# \## Requirements

# 

# Install these before running the project:

# 

# \- Android Studio

# \- Node.js

# \- npm

# \- Android Emulator

# \- Groq API key

# \- Stripe test secret key

# 

# \## Backend Setup

# 

# Open PowerShell and go to the backend folder:

# 

# ```powershell

# cd learning-ai-backend

# npm install

# ```

# 

# Create a `.env` file inside `learning-ai-backend`.

# 

# Use `.env.example` as a guide:

# 

# ```env

# GROQ\_API\_KEY=your\_groq\_api\_key\_here

# STRIPE\_SECRET\_KEY=your\_stripe\_secret\_key\_here

# PORT=3000

# ```

# 

# Start the backend:

# 

# ```powershell

# npm start

# ```

# 

# Expected result:

# 

# ```text

# Learning AI backend running on http://localhost:3000

# ```

# 

# Keep this terminal open while using the Android app.

# 

# \## Android Setup

# 

# Open the project in Android Studio.

# 

# Build the app:

# 

# ```powershell

# .\\gradlew assembleDebug

# ```

# 

# Run the app on an Android Emulator.

# 

# The app uses this backend URL for emulator testing:

# 

# ```text

# http://10.0.2.2:3000/

# ```

# 

# \## How to Use the App

# 

# 1\. Sign up with a username and email.

# 2\. Select your learning interests.

# 3\. Log in.

# 4\. Choose a task from the Home screen.

# 5\. Answer the quiz questions.

# 6\. Use \*\*Get Hint\*\* if you need AI help.

# 7\. Submit the task.

# 8\. Use \*\*Explain My Answer\*\* to get AI feedback.

# 9\. Open \*\*Learning History\*\* to review saved records.

# 10\. Open \*\*Profile\*\* to view progress and share your profile.

# 11\. Open \*\*Upgrade Account\*\* to test Stripe payment.

# 

# \## Stripe Test Payment

# 

# Use this test card:

# 

# ```text

# Card number: 4242 4242 4242 4242

# Expiry: 12/34

# CVC: 123

# Postcode: 3000

# ```

# 

# After successful payment, the Profile screen will show the updated account plan.

# 

# \## Notes

# 

# \- This app uses Stripe Test Mode only.

# \- No real money is charged.

# \- The backend must be running for AI and payment features to work.

# \- Do not upload `.env` to GitHub.

# \- Do not upload `local.properties` to GitHub.


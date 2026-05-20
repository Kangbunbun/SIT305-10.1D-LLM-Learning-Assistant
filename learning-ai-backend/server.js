import express from "express";
import cors from "cors";
import dotenv from "dotenv";
import Groq from "groq-sdk";
import Stripe from "stripe";

dotenv.config();

const app = express();

app.use(cors());
app.use(express.json());

// ---------- ENV CHECKS ----------
if (!process.env.GROQ_API_KEY) {
  console.error("Missing GROQ_API_KEY in .env file.");
  process.exit(1);
}

if (!process.env.STRIPE_SECRET_KEY) {
  console.error("Missing STRIPE_SECRET_KEY in .env file.");
  process.exit(1);
}

// ---------- GROQ SETUP ----------
const groq = new Groq({
  apiKey: process.env.GROQ_API_KEY,
});

// ---------- STRIPE SETUP ----------
const stripe = new Stripe(process.env.STRIPE_SECRET_KEY);

// ---------- AI HELPER ----------
async function askGroq(prompt) {
  if (!prompt || prompt.trim().length === 0) {
    throw new Error("Prompt is empty.");
  }

  const completion = await groq.chat.completions.create({
    model: "llama-3.3-70b-versatile",
    messages: [
      {
        role: "system",
        content:
          "You are a helpful learning assistant for students. Keep answers short, clear, beginner-friendly, and focused on learning.",
      },
      {
        role: "user",
        content: prompt,
      },
    ],
    temperature: 0.4,
    max_completion_tokens: 180,
  });

  return completion.choices?.[0]?.message?.content || "";
}

// ---------- AI ROUTES ----------
app.post("/ai/hint", async (req, res) => {
  try {
    const { prompt } = req.body;
    const response = await askGroq(prompt);

    if (!response.trim()) {
      return res.status(500).json({
        response: "",
        error: "Empty AI response.",
      });
    }

    res.json({ response });
  } catch (error) {
    res.status(500).json({
      response: "",
      error: error.message || "Failed to generate hint.",
    });
  }
});

app.post("/ai/explain", async (req, res) => {
  try {
    const { prompt } = req.body;
    const response = await askGroq(prompt);

    if (!response.trim()) {
      return res.status(500).json({
        response: "",
        error: "Empty AI response.",
      });
    }

    res.json({ response });
  } catch (error) {
    res.status(500).json({
      response: "",
      error: error.message || "Failed to generate explanation.",
    });
  }
});

// ---------- PAYMENT ROUTES ----------
function getPlanAmount(plan) {
  switch (plan) {
    case "Intermediate":
      return 499; // AUD $4.99
    case "Advanced":
      return 999; // AUD $9.99
    default:
      return null;
  }
}

app.post("/payment/create-payment-intent", async (req, res) => {
  try {
    const { plan } = req.body;

    if (!plan) {
      return res.status(400).json({
        error: "Plan is required.",
      });
    }

    const amount = getPlanAmount(plan);

    if (!amount) {
      return res.status(400).json({
        error: "Invalid paid plan. Only Intermediate and Advanced require payment.",
      });
    }

    const paymentIntent = await stripe.paymentIntents.create({
      amount,
      currency: "aud",
      description: `LLM Learning Assistant - ${plan} plan`,
      metadata: {
        plan,
        app: "LLM-Enhanced Learning Assistant App",
      },
      automatic_payment_methods: {
        enabled: true,
      },
    });

    res.json({
      clientSecret: paymentIntent.client_secret,
      plan,
      amount,
      currency: "aud",
    });
  } catch (error) {
    res.status(500).json({
      error: error.message || "Failed to create payment intent.",
    });
  }
});

// ---------- HEALTH CHECK ----------
app.get("/", (req, res) => {
  res.send("Learning AI backend is running.");
});

const port = process.env.PORT || 3000;

app.listen(port, () => {
  console.log(`Learning AI backend running on http://localhost:${port}`);
});
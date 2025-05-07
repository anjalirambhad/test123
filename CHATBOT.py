
import random
import re
from sentence_transformers import SentenceTransformer, util
import tkinter as tk
from tkinter import scrolledtext



# Load pre-trained BERT model
model = SentenceTransformer('all-MiniLM-L6-v2')

# Intent dictionary
intents = {
    "greeting": {
        "patterns": ["Hi", "Hello", "Hey there", "Heyyaa", "What's up?", "Yo", "Howdy", "Hola"],
        "responses": [
            "Hi! How can I assist you today?",
            "Hello there! Need help with something?",
            "Hey! What can I do for you?",
            "Yo! How can I help?"
        ]
    },
    "order_status": {
        "patterns": ["Where is my order?", "Track my order", "Order status please", "Where's my stuff?", "Is my order here yet?"],
        "responses": [
            "Please share your order ID so I can check it for you.",
            "Sure! What’s your order ID?",
            "Let me know your order number to help you track it."
        ]
    },
    "cancel_order": {
        "patterns": ["Cancel my order", "I want to cancel", "Can I cancel this order?", "I changed my mind", "How do I cancel the order?"],
        "responses": [
            "To cancel, go to 'My Orders' and choose 'Cancel Order'.",
            "Yes, you can cancel if it hasn't been shipped yet.",
            "Just head to your orders page and select 'Cancel'."
        ]
    },
    "return_policy": {
        "patterns": ["How to return?", "Return policy?", "Can I return my item?", "What is the return policy?", "How do I return an item?"],
        "responses": [
            "You can return within 30 days of delivery.",
            "Check your order details to initiate a return.",
            "Returns are accepted within 30 days — make sure the item is unused!"
        ]
    },
    "thanks": {
        "patterns": ["Thanks", "Thank you", "Much appreciated", "Thanks a lot", "Thanks for your help"],
        "responses": [
            "You're most welcome!",
            "Happy to help!",
            "Glad I could assist!"
        ]
    },
    "goodbye": {
        "patterns": ["Bye", "Goodbye", "See you later", "Take care", "Catch you later"],
        "responses": [
            "Thanks for visiting! Hope to see you again soon!",
            "Goodbye! Let us know if you need anything else.",
            "Take care! Come back anytime."
        ]
    }
}

# Flatten patterns and tags
all_patterns = []
tags = []
for tag, data in intents.items():
    for pattern in data["patterns"]:
        all_patterns.append(pattern)
        tags.append(tag)

# Encode all patterns once
pattern_embeddings = model.encode(all_patterns, convert_to_tensor=True)

# Context tracker
context = {
    "expecting_order_id": False,
    "last_intent": None
}

def preprocess(text):
    return re.sub(r"[^\w\s]", "", text.lower()).strip()

def get_response(user_input):
    clean_input = preprocess(user_input)

    if context["expecting_order_id"]:
        context["expecting_order_id"] = False
        return f"You can check the status of your order (ID: {clean_input}) by visiting your Orders Page:\n👉 https://www.example.com/myorders"

    user_embedding = model.encode(clean_input, convert_to_tensor=True)
    similarities = util.cos_sim(user_embedding, pattern_embeddings)[0]
    best_match = similarities.argmax().item()
    confidence = similarities[best_match].item()

    if confidence < 0.5:
        return "Sorry, I didn't quite get that. Could you please rephrase your request?"

    matched_tag = tags[best_match]

    if matched_tag == "order_status":
        context["expecting_order_id"] = True

    context["last_intent"] = matched_tag

    return random.choice(intents[matched_tag]["responses"])

# ----------- UI with better design -----------

def reply():
    msg = entry.get()
    if msg.strip() == "":
        return
    chat.config(state=tk.NORMAL)
    chat.insert(tk.END, "You: " + msg + "\n")
    entry.delete(0, tk.END)
    if msg.lower() == "quit":
        chat.insert(tk.END, "Bot: Goodbye!\n")
        root.destroy()
    else:
        chat.insert(tk.END, "Bot: " + get_response(msg) + "\n")
    chat.config(state=tk.DISABLED)
    chat.yview(tk.END)

root = tk.Tk()
root.title("E-commerce ChatBot")
root.geometry("500x500")
root.resizable(False, False)

# Chat area with scrollbar
chat = scrolledtext.ScrolledText(root, wrap=tk.WORD, font=("Arial", 12), bg="white", state=tk.DISABLED)
chat.pack(padx=10, pady=10, fill=tk.BOTH, expand=True)

# Bottom input area
frame = tk.Frame(root)
frame.pack(padx=10, pady=10, fill=tk.X)

entry = tk.Entry(frame, font=("Arial", 12))
entry.pack(side=tk.LEFT, fill=tk.X, expand=True, padx=(0, 10))

send_button = tk.Button(frame, text="Send", bg="#007acc", fg="white", font=("Arial", 12), command=reply)
send_button.pack(side=tk.RIGHT)

chat.config(state=tk.NORMAL)
chat.insert(tk.END, "Bot: Hello! How can I help you today?\n")
chat.config(state=tk.DISABLED)

root.mainloop()

# pip install sentence-transformers

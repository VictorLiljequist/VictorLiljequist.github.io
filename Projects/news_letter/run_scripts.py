import os
import subprocess
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.base import MIMEBase
from email import encoders
import time


scripts_folder = "./scripts"

# Get all Python scripts in the folder
all_scripts = [script for script in os.listdir(scripts_folder) if script.endswith(".py")]

# Separate print_pdf.py from the rest
print_pdf_script = "print_pdf.py"
other_scripts = [script for script in all_scripts if script != print_pdf_script]

# Run all other scripts first
for script_name in other_scripts:
    script_path = os.path.join(scripts_folder, script_name)
    print(f"Running script: {script_name}")
    
    try:
        subprocess.run(["python", script_path], check=True)
    except subprocess.CalledProcessError as e:
        print(f"Error running {script_name}: {e}")

# Run print_pdf.py last
if print_pdf_script in all_scripts:
    print_pdf_path = os.path.join(scripts_folder, print_pdf_script)
    print(f"Running script: {print_pdf_script}")
    
    try:
        subprocess.run(["python", print_pdf_path], check=True)
    except subprocess.CalledProcessError as e:
        print(f"Error running {print_pdf_script}: {e}")
else:
    print(f"Script '{print_pdf_script}' not found in the folder.")



    
def sendEmail():
    sender_email = "totosnewsbot@gmail.com"
    receiver_email = "victor.liljequist@gmail.com"
    password = ""
    subject = "Market Report"
    body = "Please find the attached market data report."
    output_file = "./scripts/final.pdf" 

    # Create the email message
    msg = MIMEMultipart()
    msg['From'] = sender_email
    msg['To'] = receiver_email
    msg['Subject'] = subject
    msg.attach(MIMEText(body, 'plain'))

    # Attach the text file
    try:
        with open(output_file, "rb") as attachment:
            part = MIMEBase('application', 'octet-stream')
            part.set_payload(attachment.read())
        encoders.encode_base64(part)
        part.add_header('Content-Disposition', f"attachment; filename={output_file}")
        msg.attach(part)
    except FileNotFoundError:
        print(f"Error: {output_file} not found")
        return

    # Send the email
    try:
        server = smtplib.SMTP('smtp.gmail.com', 587)
        server.starttls()  # Start TLS for security
        server.login(sender_email, password)
        text = msg.as_string()
        server.sendmail(sender_email, receiver_email, text)
        print("Email sent successfully!")
    except Exception as e:
        print(f"Error: {e}")
    finally:
        server.quit()

sendEmail()

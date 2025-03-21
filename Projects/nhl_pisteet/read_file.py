import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from datetime import datetime, timedelta
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.base import MIMEBase
from email import encoders
import time

# Set up Chrome options
chrome_options = Options()
chrome_options.add_argument("--headless")  # Run in headless mode
chrome_options.add_argument("--disable-blink-features=AutomationControlled")
chrome_options.add_argument("--log-level=3")
chrome_options.add_experimental_option("excludeSwitches", ["enable-logging"])

service = Service("/usr/local/bin/chromedriver")

driver = webdriver.Chrome(service=service, options=chrome_options)
get_date = datetime.today() - timedelta(days=1)
today = get_date.strftime('%Y-%m-%d')

url = f"https://www.nhl.com/stats/skaters?reportType=game&dateFrom={today}&dateTo={today}&gameType=2&nationalityCode=FIN&sort=points,goals,assists&page=0&pageSize=50"
driver.get(url)
data = []
try:
    WebDriverWait(driver, 10).until(
        EC.presence_of_all_elements_located((By.CSS_SELECTOR, '.sc-hZxEoA, .iWQWig, .rt-td, .left-aligned, .pinned-column, .last-left-pinned-column'))
    )
    rows = driver.find_elements(By.CSS_SELECTOR, '.sc-hZxEoA, .iWQWig, .rt-tr')  
    data = []
    
    for row in rows:
        cells = row.find_elements(By.CSS_SELECTOR, '.rt-td, .left-aligned, .pinned-column, .last-left-pinned-column')  
        if len(cells) > 6:  
            name = cells[1].text.strip()    
            assists = cells[5].text.strip() 
            goals = cells[6].text.strip()  
            data.append([name, assists, goals]) 
            
    data_array = np.array(data)
    df = pd.DataFrame(data_array, columns=["Name", "Goals", "Assists"])

    def save_table_as_image(df, filename):
        table_width = 10
        row_height = 0.6  
        fig, ax = plt.subplots(figsize=(table_width, len(df) * row_height + 1)) 
        ax.axis('tight')
        ax.axis('off')

        header_bg_color = "#2C3E50" 
        header_text_color = "white"
        row_alt_color_1 = "#F9F9F9"  
        row_alt_color_2 = "white"
        grid_color = "grey"
        text_color = "#34495E"

        row_colors = [row_alt_color_1 if i % 2 == 0 else row_alt_color_2 for i in range(len(df))]
        cell_colours = [[row_colors[i]] * len(df.columns) for i in range(len(df))]

        assert len(cell_colours) == len(df), f"Expected {len(df)} rows in cell_colours, but got {len(cell_colours)}"

        table = ax.table(cellText=df.values, colLabels=df.columns, cellColours=cell_colours, cellLoc='center', loc='center')

        table.auto_set_font_size(False)
        table.set_fontsize(12)

        for i in range(len(df.columns)):
            cell = table[0, i]
            cell.set_text_props(weight="bold", fontsize=14, color=header_text_color)
            cell.set_facecolor(header_bg_color)

        for (i, j), cell in table.get_celld().items():
            cell.set_edgecolor(grid_color)  # Grid color
            cell.set_text_props(color=text_color if i > 0 else header_text_color)

        for key, cell in table.get_celld().items():
            cell.set_height(row_height / len(df))  # Adjust cell height

        plt.savefig(filename, dpi=300, bbox_inches='tight', transparent=True)
        plt.close()

    save_table_as_image(df, "pisteet.png")
    print("✅ Professional table saved as PNG successfully!")

    def sendEmail():
        sender_email = "totosnewsbot@gmail.com"
        receiver_emails = [""]
        password = "" 
        subject = "YÖN NHL PISTEET"
        body = ""
        output_file = "pisteet.png" 

        msg = MIMEMultipart()
        msg['From'] = sender_email
        msg['To'] = ", ".join(receiver_emails)
        msg['Subject'] = subject
        msg.attach(MIMEText(body, 'plain'))

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
        
        try:
            server = smtplib.SMTP('smtp.gmail.com', 587)
            server.starttls()  # Start TLS for security
            server.login(sender_email, password)
            text = msg.as_string()
            server.sendmail(sender_email, receiver_emails, text)
            print("Email sent successfully! to: " + ", ".join(receiver_emails))
        except Exception as e:
            print(f"Error: {e}")
        finally:
            server.quit()
    sendEmail()

except Exception as e:
    print(f"No data found and no email sent!")
finally:
    driver.quit()
    

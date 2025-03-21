import time
import numpy as np
import matplotlib.pyplot as plt
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from reportlab.lib.pagesizes import letter
from reportlab.lib import colors
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle
from reportlab.lib.enums import TA_CENTER, TA_LEFT


# Set up Chrome options
CHROMEDRIVER_PATH = "/usr/local/bin/chromedriver"  # Adjust as needed
NEWS_URL = "https://www.cnbc.com/"
PDF_OUTPUT_FILE = "./scripts/news_report.pdf"

# ---- SELENIUM SETUP ----

def setup_driver():
    """Initialize and return a headless Selenium WebDriver."""
    chrome_options = Options()
    chrome_options.add_argument("--headless")  # Run in headless mode
    service = Service(CHROMEDRIVER_PATH)
    return webdriver.Chrome(service=service, options=chrome_options)

# ---- SCRAPING FUNCTION ----

def scrape_news(driver):
    """Scrape the latest and trending news headlines from CNBC."""
    driver.get(NEWS_URL)
    
    try:
        # Wait for the "Latest News" button and click it multiple times to load more news
        load_more_button = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.CSS_SELECTOR, ".LatestNews-button"))
        )
        for _ in range(4):  
            driver.execute_script("arguments[0].click();", load_more_button)
            time.sleep(1)
        
        # Extract news headlines
        latest_news = [news.text.strip() for news in driver.find_elements(By.CSS_SELECTOR, ".LatestNews-headline")]
        trending_news = [news.text.strip() for news in driver.find_elements(By.CSS_SELECTOR, ".TrendingNowItem-title")]

        return latest_news, trending_news

    except Exception as e:
        print(f"Error while scraping: {e}")
        return [], []
    finally:
        driver.quit()  # Ensure browser is closed

# ---- PDF GENERATION ----

def create_pdf(latest_news, trending_news):
    """Generate a structured PDF report with a table format for news."""
    pdf = SimpleDocTemplate(PDF_OUTPUT_FILE, pagesize=letter)
    styles = getSampleStyleSheet()
    
    # Custom styles
    title_style = ParagraphStyle("Title", fontSize=28, textColor=colors.HexColor("#2C3E50"), alignment=TA_CENTER, spaceAfter=12, fontName="Helvetica-Bold")
    section_style = ParagraphStyle("Section", fontSize=20, textColor=colors.HexColor("#2C3E50"), alignment=TA_LEFT, spaceAfter=12, fontName="Helvetica-Bold")
    news_item_style = ParagraphStyle("NewsItem", fontSize=16, textColor=colors.HexColor("#34495E"), alignment=TA_LEFT, spaceAfter=6, fontName="Helvetica", leading=20)

    story = [
        Paragraph("TOTO News Report", title_style),
        Paragraph("Your Daily News Digest", title_style),
        Spacer(1, 24),
    ]

    # Trending News Table
    if trending_news:
        story.append(Paragraph("TRENDING NEWS", section_style))
        story.append(Spacer(1, 12))
        
        trending_data = [["#", "Headline"]]  # Table header
        trending_data += [[str(i+1), Paragraph(news, news_item_style)] for i, news in enumerate(trending_news[:4])]

        trending_table = Table(trending_data, colWidths=[40, 450])
        trending_table.setStyle(TableStyle([
            ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#2C3E50")),  # Header Background
            ("TEXTCOLOR", (0, 0), (-1, 0), colors.white),  # Header Text Color
            ("ALIGN", (0, 0), (-1, -1), "LEFT"),
            ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
            ("FONTNAME", (0, 1), (-1, -1), "Helvetica"),
            ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
            ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.HexColor("#F9F9F9"), colors.white]),
            ("GRID", (0, 0), (-1, -1), 0.5, colors.grey),
            ('BOTTOMPADDING', (0, 0), (-1, -1), 10),  # Increase bottom padding
            ('TOPPADDING', (0, 0), (-1, -1), 10),  # Increase top padding
        ]))

        story.append(trending_table)
        story.append(Spacer(1, 12))

    # Latest News Table
    if latest_news:
        story.append(Paragraph("LATEST NEWS", section_style))
        story.append(Spacer(1, 12))
        
        latest_data = [["#", "Headline"]]  # Table header
        latest_data += [[str(i+1), Paragraph(news, news_item_style)] for i, news in enumerate(latest_news[:20])]

        latest_table = Table(latest_data, colWidths=[40, 450])
        latest_table.setStyle(TableStyle([
            ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#2C3E50")),  # Header Background
            ("TEXTCOLOR", (0, 0), (-1, 0), colors.white),  # Header Text Color
            ("ALIGN", (0, 0), (-1, -1), "LEFT"),
            ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
            ("FONTNAME", (0, 1), (-1, -1), "Helvetica"),
            ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
            ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.HexColor("#F9F9F9"), colors.white]),
            ("GRID", (0, 0), (-1, -1), 0.5, colors.grey),
            ('BOTTOMPADDING', (0, 0), (-1, -1), 10),  # Increase bottom padding
            ('TOPPADDING', (0, 0), (-1, -1), 10),  # Increase top padding
        ]))

        story.append(latest_table)
        story.append(Spacer(1, 12))

    # Generate the PDF
    pdf.build(story)
    print(f"✅ PDF saved as {PDF_OUTPUT_FILE}")

# ---- MAIN EXECUTION ----

if __name__ == "__main__":
    driver = setup_driver()
    latest_news, trending_news = scrape_news(driver)
    create_pdf(latest_news, trending_news)


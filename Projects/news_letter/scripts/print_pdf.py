import pandas as pd
import matplotlib.pyplot as plt
from PyPDF2 import PdfMerger
from fpdf import FPDF
import os
class PDF(FPDF):
    def header(self):
        self.set_font('Helvetica', 'B', 12)  # Use Helvetica Bold for header
        self.cell(0, 10, 'Market Data Report', 0, 1, 'C')
        self.ln(10)

    def footer(self):
        self.set_y(-15)
        self.set_font('Helvetica', 'I', 8)  # Use Helvetica Italic for footer
        self.cell(0, 10, f'Page {self.page_no()}', 0, 0, 'C')

    def chapter_title(self, title):
        self.set_font('Helvetica', 'B', 12)  # Use Helvetica Bold for chapter titles
        self.cell(0, 10, title, 0, 1, 'L')
        self.ln(4)

    def add_plot(self, image_path):
        fixed_width = 180
        self.image(image_path, x=10, y=self.get_y(), w=fixed_width)
        self.ln(85)  # Adjust spacing after the image

# Create PDF
pdf = PDF()
pdf.add_page()
pdf.chapter_title("US Markets")
pdf.add_plot("./plots/us_markets.png")
pdf.add_plot("./plots/sector_data.png")
pdf.add_page()


# Add Top Gainers Plot
pdf.chapter_title("Top Gainers/Losers")
pdf.add_plot("./plots/us_top_gainers.png")
pdf.add_plot("./plots/us_top_losers.png")

# Add US Markets Plot

# Add EU Markets Plot
pdf.add_page()
pdf.chapter_title("EU Markets")
pdf.add_plot("./plots/eu_markets.png")
pdf.add_plot("./plots/eu_top_gainers.png")
pdf.add_plot("./plots/eu_top_losers.png")



# Add Asia Markets Plot
pdf.add_page()
pdf.chapter_title("Asia Markets")
pdf.add_plot("./plots/asia_markets.png")
pdf.add_plot("./plots/asia_top_gainers.png")
pdf.add_plot("./plots/asia_top_losers.png")

# Add Sector Data Plot


# Save PDF
pdf.output("./scripts/market_data_report_with_all_plots.pdf")

def merge_pdfs():
    merger = PdfMerger()
    merger.append("./scripts/news_report.pdf")
    merger.append("./scripts/market_data_report_with_all_plots.pdf")
    merger.write("./scripts/final.pdf")
    merger.close()

merge_pdfs()

print("PDF with all plots generated successfully!")
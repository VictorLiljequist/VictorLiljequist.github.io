import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# Set up Chrome options
chrome_options = webdriver.ChromeOptions()
chrome_options.add_argument("--headless")  # Run in headless mode
chrome_options.add_argument("--no-sandbox")
chrome_options.add_argument("--disable-dev-shm-usage")

service = Service("/usr/local/bin/chromedriver")
driver = webdriver.Chrome(service=service, options=chrome_options)

# Visit the target URL
url = "https://www.cnbc.com/us-market-movers/"
driver.get(url)

gainers = []
losers = []
try:
    # Wait for the page to load completely
    WebDriverWait(driver, 10).until(
        EC.presence_of_all_elements_located((By.CSS_SELECTOR, '.MarketTop-name'))
    )
    WebDriverWait(driver, 10).until(
        EC.presence_of_all_elements_located((By.CSS_SELECTOR, ".MarketTop-value, .MarketTop-quoteGain, .MarketTop-quoteChange"))
    )
    
    WebDriverWait(driver, 10).until(
        EC.presence_of_all_elements_located((By.CSS_SELECTOR, ".MarketTop-value, .MarketTop-quoteDecline, .MarketTop-quoteChange"))
    )
    
    symbols = driver.find_elements(By.CSS_SELECTOR, '.MarketTop-name')
    gains = driver.find_elements(By.CSS_SELECTOR, ".MarketTop-quoteGain")
    losses = driver.find_elements(By.CSS_SELECTOR, ".MarketTop-quoteDecline")
    
    if symbols and gains:
        # Process first 12 symbols for gainers
        for i, symbol in enumerate(symbols[:12]):  # Process first 12 symbols
            symbol_text = symbol.text.strip()
            percentage_gain = gains[i].text.strip()  # Correct reference to gains[i]
            gainers.append([symbol_text, percentage_gain])
    
    if symbols and losses:
        # Process remaining symbols for losers
        for i, symbol in enumerate(symbols[12:]):  # Process remaining symbols
            symbol_text = symbol.text.strip()
            percentage_loss = losses[i].text.strip()  # Correct reference to losses[i]
            losers.append([symbol_text, percentage_loss])
except Exception as e:
    print(f"Error: {e}")
finally:
    driver.quit()

# Convert data to numpy array
gainers = np.array(gainers)
losers = np.array(losers)

us_top_gainers_df = pd.DataFrame(gainers, columns=["Symbol","Percentage Gain"])
us_top_losers_df = pd.DataFrame(losers, columns=["Symbol","Percentage Loss"])

def format_percentage(df, column_name, is_gainers=True):
    df[column_name] = df[column_name].str.rstrip('%').astype(float)
    if is_gainers:
        df[column_name] = df[column_name].apply(lambda x: f"+{x:.1f}%")
    else:
        df[column_name] = df[column_name].apply(lambda x: f"-{x:.1f}%")  
    return df


# Format the percentage columns
us_top_gainers_df = format_percentage(us_top_gainers_df, "Percentage Gain", is_gainers=True)
us_top_losers_df = format_percentage(us_top_losers_df, "Percentage Loss", is_gainers=False)


def save_table_as_image(df, filename):
    table_width = 10
    # Increase the vertical space by adjusting the figure height
    row_height = 0.6  # Increase this value to add more space between rows
    fig, ax = plt.subplots(figsize=(table_width, len(df) * row_height + 1))  # Dynamic height with more space
    ax.axis('tight')
    ax.axis('off')

    # Define color scheme
    header_bg_color = "#2C3E50"  # Dark blue
    header_text_color = "white"
    row_alt_color_1 = "#F9F9F9"  # Light grey
    row_alt_color_2 = "white"
    grid_color = "grey"
    text_color = "#34495E"

    # Create row colors (excluding header)
    row_colors = [row_alt_color_1 if i % 2 == 0 else row_alt_color_2 for i in range(len(df))]
    # Create cell_colours for data rows only
    cell_colours = [[row_colors[i]] * len(df.columns) for i in range(len(df))]

    # Debugging: Check DataFrame shape and cell_colours length
    print(f"DataFrame shape: {df.shape}")
    print(f"cell_colours length: {len(cell_colours)}")

    # Check that the cell_colours has the correct number of rows
    assert len(cell_colours) == len(df), f"Expected {len(df)} rows in cell_colours, but got {len(cell_colours)}"

    # Create the table with colors
    table = ax.table(cellText=df.values, colLabels=df.columns, cellColours=cell_colours, cellLoc='center', loc='center')

    # Apply styling
    table.auto_set_font_size(False)
    table.set_fontsize(12)

    # Header styling
    for i in range(len(df.columns)):
        cell = table[0, i]
        cell.set_text_props(weight="bold", fontsize=14, color=header_text_color)
        cell.set_facecolor(header_bg_color)

    # Set text color and borders
    for (i, j), cell in table.get_celld().items():
        cell.set_edgecolor(grid_color)  # Grid color
        cell.set_text_props(color=text_color if i > 0 else header_text_color)

    # Manually adjust row heights (optional)
    for key, cell in table.get_celld().items():
        cell.set_height(row_height / len(df))  # Adjust cell height

    # Save as PNG
    plt.savefig(filename, dpi=300, bbox_inches='tight', transparent=True)
    plt.close()


# Save table as PNG
save_table_as_image(us_top_gainers_df, "./plots/us_top_gainers.png")
save_table_as_image(us_top_losers_df, "./plots/us_top_losers.png")


print("✅ Professional table saved as PNG successfully!")



import os
import pandas as pd
import matplotlib.pyplot as plt
import logging

logging.basicConfig(level=logging.INFO)
def find_csv_filename(path):
    for file in os.listdir(path):
        if file.endswith('.csv'):
            return file
    return None

def generate_histogram(directory, x_title, y_title, chart_title):
    csv_file = find_csv_filename(directory)
    if csv_file is None:
        logging.error("No CSV file found in the directory.")
        return

    logging.info(f"Found CSV file: {csv_file}")

    file_path = os.path.join(directory, csv_file)
    data = pd.read_csv(file_path, delimiter='|')

    categories = data.iloc[:, 0]
    values = data.iloc[:, 1]
    logging.info("Data processing successful.")
    try:
        plt.figure(figsize=(10, 6))
        plt.bar(categories, values)
        plt.title(chart_title)
        plt.xlabel(x_title)
        plt.ylabel(y_title)
        plt.xticks(rotation=45, ha='right')
        plt.tight_layout()
        plot_file_name = os.path.join(directory, 'histogram.png')
        plt.savefig(plot_file_name)
        plt.close()
        logging.info(f"Histogram plot successfully saved at {plot_file_name}")
    except Exception as e:
        logging.error("Failed to create or save the plot: {e}")

if __name__ == "__main__":
    import sys
    if len(sys.argv) != 5:
        print("Usage: python histogram_chart.py <directory_path> <x_title> <y_title> <chart_title>")
    else:
        directory_path, x_title, y_title, chart_title = sys.argv[1:]
        generate_histogram(directory_path, x_title, y_title, chart_title)

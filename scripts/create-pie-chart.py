import os
import pandas as pd
import matplotlib.pyplot as plt
import sys
import logging

logging.basicConfig(level=logging.INFO)
def find_csv_filename(path):
    for file in os.listdir(path):
        if file.endswith('.csv'):
            return file
    return None

def generate_pie_chart(directory, chart_title):
    csv_file = find_csv_filename(directory)
    if csv_file is None:
        logging.error("No CSV file found in the directory.")
        return
    logging.info(f"Found CSV file: {csv_file}")

    file_path = os.path.join(directory, csv_file)
    data = pd.read_csv(file_path, delimiter='|')
    logging.info("CSV file successfully read")
    labels = data.iloc[:, 0]
    sizes = data.iloc[:, 1]

    plt.figure(figsize=(15, 15))
    plt.pie(sizes, labels=labels, autopct='%1.1f%%')
    plt.title(chart_title)
    plot_file_name = os.path.join(directory, 'pie_chart.png')
    plt.savefig(plot_file_name)
    plt.close()
    logging.info(f"Pie chart successfully saved at {plot_file_name}")

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python pie_chart.py <directory_path> <chart_title>")
    else:
        directory_path, chart_title = sys.argv[1:]
        generate_pie_chart(directory_path, chart_title)

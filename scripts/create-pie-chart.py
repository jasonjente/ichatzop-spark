# pie_chart.py
import os
import pandas as pd
import matplotlib.pyplot as plt

def find_csv_filename(path):
    for file in os.listdir(path):
        if file.endswith('.csv'):
            return file
    return None

def generate_pie_chart(directory, chart_title):
    csv_file = find_csv_filename(directory)
    if csv_file is None:
        print("No CSV file found in the directory.")
        return

    # Load data
    file_path = os.path.join(directory, csv_file)
    data = pd.read_csv(file_path, delimiter='|')
    labels = data.iloc[:, 0]
    sizes = data.iloc[:, 1]

    # Create Pie Chart
    plt.figure(figsize=(15, 15))
    plt.pie(sizes, labels=labels, autopct='%1.1f%%')
    plt.title(chart_title)
    plt.savefig(os.path.join(directory, 'pie_chart.png'))
    plt.close()
    print("Pie chart created: " + file_path + "/pie_chart.png")

if __name__ == "__main__":
    import sys
    if len(sys.argv) != 3:
        print("Usage: python pie_chart.py <directory_path> <chart_title>")
    else:
        directory_path, chart_title = sys.argv[1:]
        generate_pie_chart(directory_path, chart_title)

from os.path import abspath, join
import pandas as pd
import numpy as np
from plotnine import ggplot, geom_bar, aes, scale_x_discrete, theme, element_text, labs, \
    geom_histogram, facet_wrap, element_blank, scale_x_continuous, scale_y_continuous, geom_line, \
    geom_boxplot


def histogram_n_most_frequent_all(df, n=50):
    most_frequent_all = df.groupby(["name", "external"]).size().reset_index(name="count")

    nlargest = most_frequent_all.nlargest(n, "count")

    print(
        ggplot(nlargest)
        + labs(x=str(n) + " Most frequent words",
               y="Occurrences in project",
               title="All projects - most frequent words")
        + scale_x_discrete(limits=nlargest['name'].tolist())
        + geom_bar(aes(x='name', y='count', fill="external"), stat='identity')
        + theme(axis_text_x=element_text(rotation=90))
    )


def histogram_n_most_frequent_per_project(df, n=50):
    most_frequent_all = df.groupby(["name", "project", "external"]).size().reset_index(name="count")

    for project_name in most_frequent_all.project.unique():
        project = most_frequent_all[most_frequent_all["project"] == project_name]
        nlargest_per_project = project.nlargest(n, "count")
        print(
            ggplot(nlargest_per_project)
            + labs(x=str(n) + " Most frequent words",
                   y="Occurrences in project",
                   title=project_name + " - most frequent words")
            + scale_x_discrete(limits=nlargest_per_project['name'].tolist())
            + geom_bar(aes(x='name', y='count', fill="external"), stat='identity')
            + theme(axis_text_x=element_text(rotation=90))
        )


# Prints the frequency of each word in the corpus, divided by the frequency of the most common word
def most_frequent_words_normalized(df, n=None):
    most_frequent_all = df.groupby(["name", "external"]).size() \
        .reset_index(name="count") \
        .sort_values("count", ascending=False)
    if n is not None:
        most_frequent_all = most_frequent_all.nlargest(n, 'count')
    most_frequent_word_occurrences = int(most_frequent_all[:1]['count'])

    most_frequent_all['frequency'] = most_frequent_all['count'] / most_frequent_word_occurrences
    data_length = n or most_frequent_all.shape[0]
    most_frequent_all['rank'] = np.linspace(1, data_length, data_length)

    print(ggplot(most_frequent_all)
          + labs(x="log(rank)",
                 y="log(frequency in project)",
                 title="Word Frequency - All Projects, Unfiltered")
          + scale_x_continuous(trans='log2') + scale_y_continuous(trans='log2')
          + geom_line(aes(x='rank', y='frequency'), stat='identity')
          + theme(axis_text_x=element_text(rotation=90))
          )
    print(ggplot(most_frequent_all[most_frequent_all["external"] == False])
          + labs(x="log(rank)",
                 y="log(frequency in project)",
                 title="Word Frequency - All Projects, Filtered")
          + scale_x_continuous(trans='log2') + scale_y_continuous(trans='log2')
          + geom_line(aes(x='rank', y='frequency'), stat='identity')
          + theme(axis_text_x=element_text(rotation=90))
          )


# Prints the frequency of each word in the corpus, divided by the frequency of the most common word
def frequent_to_length_correlation(df, n=None):
    freq_length_external_all = df.groupby(["length", "external"]) \
        .size() \
        .reset_index(name="count") \
        .sort_values("count", ascending=False)
    most_frequent_length_occurrences = int(freq_length_external_all[:1]['count'])

    freq_length_external_all['frequency'] = \
        freq_length_external_all['count'] / most_frequent_length_occurrences
    data_length = freq_length_external_all.shape[0]
    freq_length_external_all['rank'] = np.linspace(1, data_length, data_length)

    bins = 10
    freq_length_external_all["bin"] = (freq_length_external_all["length"] // (
            freq_length_external_all["length"].max() // bins)).astype("int").astype("category")

    print(ggplot(freq_length_external_all)
          + labs(x="log(rank)",
                 y="log(frequency in project)",
                 title="Word Length Frequency - All Projects, Unfiltered")
          + scale_x_continuous(trans='log2') + scale_y_continuous(trans='log2')
          + geom_line(aes(x='rank', y='frequency'), stat='identity')
          + theme(axis_text_x=element_text(rotation=90))
          )

    print(ggplot(freq_length_external_all[freq_length_external_all["external"] == False])
          + labs(x="log(rank)",
                 y="log(frequency in project)",
                 title="Word Length Frequency - All Projects, Filtered")
          + scale_x_continuous(trans='log2') + scale_y_continuous(trans='log2')
          + geom_line(aes(x='rank', y='frequency'), stat='identity')
          + theme(axis_text_x=element_text(rotation=90))
          )

    print(ggplot(freq_length_external_all)
          + labs(x="Length",
                 y="frequency in project",
                 title="Word Length Frequency - All Projects, Unfiltered")
          + geom_boxplot(aes(x='bin', y='frequency'))
          )

    print(ggplot(freq_length_external_all)
          + labs(x="Length",
                 y="log(frequency in project)",
                 title="Word Length Frequency - All Projects, Unfiltered")
          + scale_y_continuous(trans='log2')
          + geom_boxplot(aes(x='bin', y='frequency'))
          )


# Prints the frequency of each word in the corpus, divided by the frequency of the most common word
def frequent_to_length_correlation_alt(df, n=None):
    most_frequent_lengths_all = df.groupby(["length", "external", "project"]).size().reset_index(
        name="count")
    most_frequent_lengths_all = most_frequent_lengths_all[most_frequent_lengths_all["length"] <= 60]
    most_frequent_length_occurrences = int(most_frequent_lengths_all[:1]['count'])

    most_frequent_lengths_all['frequency'] = most_frequent_lengths_all['count'] / most_frequent_length_occurrences

    # print(ggplot(most_frequent_lengths_all)
    #       + labs(x="log(rank)",
    #              y="log(frequency in project)",
    #              title="Word Length Frequency - All Projects, Unfiltered")
    #       + scale_x_continuous(trans='log2') + scale_y_continuous(trans='log2')
    #       + geom_line(aes(x='rank', y='frequency'), stat='identity')
    #       + theme(axis_text_x=element_text(rotation=90))
    #       )
    #
    # print(ggplot(most_frequent_lengths_all[most_frequent_lengths_all["external"] == False])
    #       + labs(x="log(rank)",
    #              y="log(frequency in project)",
    #              title="Word Length Frequency - All Projects, Filtered")
    #       + scale_x_continuous(trans='log2') + scale_y_continuous(trans='log2')
    #       + geom_line(aes(x='rank', y='frequency'), stat='identity')
    #       + theme(axis_text_x=element_text(rotation=90))
    #       )

    most_frequent_lengths_all["length"] = most_frequent_lengths_all["length"].astype("category")

    print(ggplot(most_frequent_lengths_all)
          + labs(x="length",
                 y="log(frequency in project)",
                 title="Word Length Frequency - All Projects, Unfiltered")
          + scale_y_continuous(trans='log2')
          + geom_boxplot(aes(x='length', y='frequency', fill="external"))
          )


if __name__ == '__main__':
    path = "C:/Nitsan/third/JavaAnalyzer"
    results_file = "analyzerResults_v2.csv"
    dataframe = pd.read_csv(abspath(join(path, results_file)))
    all_frequencies = pd.read_csv(abspath(join(path, 'analyzerResults_words_ALL.csv')))
    filtered_out_words = all_frequencies[np.where(all_frequencies['external'] == 1, True, False)][
        'word']
    dataframe["external"] = dataframe['name'].isin(filtered_out_words)

    # histogram_n_most_frequent_all(dataframe)
    # histogram_n_most_frequent_per_project(dataframe)
    # most_frequent_words_normalized(dataframe)
    frequent_to_length_correlation(dataframe)
    # frequent_to_length_correlation_alt(dataframe)

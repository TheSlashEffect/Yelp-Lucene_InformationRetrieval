#!/usr/bin/python
import sys
import datetime
import mysql.connector
from mysql.connector import errorcode

config = {
    'user': 'root',
    'password': 'allPiDecimalDigits',
    'database': 'yelp_db',
    'host': '127.0.0.1',
    'raise_on_warnings': True,
}

try:
    dbCon0 = mysql.connector.connect(**config)
    dbCon1 = mysql.connector.connect(**config)
except mysql.connector.Error as err:
    if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
        print("Invalid credentials")
    elif err.errno == errorcode.ER_BAD_DB_ERROR:
        print("Database does not exist")
    else:
        print(err)
        sys.exit(-1)

cursor0 = dbCon0.cursor()
cursor1 = dbCon1.cursor()

query = "SELECT * FROM business WHERE id IN (SELECT business_id FROM category WHERE category = 'Asian Fusion' or category = 'Cambodian' or category = 'Chinese' or category = 'Filipino' or category = 'Himalayan/Nepalese' or category = 'Hong Kong Style Cafe' or category = 'Indonesian' or category = 'Japanese' or category = 'Korean' or category = 'Laotian' or category = 'Live/Raw Food' or category = 'Malaysian' or category = 'Noodles' or category = 'Pan Asia' or category = 'Polynesian' or category = 'Singaporean' or category = 'Sri Lankan' or category = 'Sushi Bars' or category = 'Thai' or category = 'Vietnamese' or category = 'Arabian' or category = 'Armenian' or category = 'Bangladeshi' or category = 'Burmese' or category = 'Indian' or category = 'Kebab' or category = 'Kosher' or category = 'Middle Eastern' or category = 'Pakistani' or category = 'Persian/Iranian' or category = 'Russian' or category = 'Syrian' or category = 'Taiwanese' or category = 'Turkish' or category = 'Uzbek' or category = 'Mexican');"
restCount = 0
count = 0

startTime = datetime.datetime.now()

try:
    cursor0.execute(query)  # cursor0 now contains 'tuples' of countries {CountryName, CountryCode}

    # Store cursor0's results in object to avoid connection timeout from server since
    # cursor0's connection will remain idle.
    results = list(cursor0)
    dbCon0.close()
    print("First subquery completed successfuly, con0 closed.")

    for row in results:
        restCount += 1
        businessName = str(row[1] + '\n')

        # Perform review query for each restaurant based on business_id.
        # To speed this method up by class orders, an indexed was added
        # on the field "business_id" in the "review" table.
        subQuery = """SELECT * FROM review WHERE business_id = '{}' ;""".format(row[0])

        # Write each review to file
        cursor1.execute(subQuery)
        for review in cursor1:
            count += 1

            # Filter illegal characters from file name
            businessNameNormalized = "".join([c for c in businessName if c.isalpha() or c.isdigit() or c == ' ']).rstrip()

            filename = str(count) + '. ' + str(review[0] + ' - ' + businessNameNormalized)
            businessId = str(review[1]) + '\n'
            userId = str(review[2]) + '\n'
            stars = str(review[3]) + '\n'
            date = str(review[4]) + '\n'
            text = str(review[5])
            useful = str(review[6]) + '\n'
            funny = str(review[7]) + '\n'
            cool = str(review[8]) + '\n'

            # Create document with review's id as filename
            docFile = open('D:/Temp/yelp/review/' + filename + '.txt', 'w', encoding='utf-8')

            docFile.write(businessId)
            docFile.write(businessName)

            docFile.write(userId)
            docFile.write(stars)
            docFile.write(date)
            docFile.write(useful)
            docFile.write(funny)
            docFile.write(cool)
            docFile.write(text)

            docFile.close()

        # Show progress to user in real time
        if restCount % 100 == 0:
            print(str(restCount) + ' restaurants\' ' + 'review files successfully written to disk')


except mysql.connector.Error as error:
    print("Error: {}".format(error))

endTime = datetime.datetime.now()

print("Task completed in " + str(endTime - startTime))

print("Count: = " + str(count))
print("restCount: = " + str(restCount))

dbCon0.close()
dbCon1.close()
print("Closed db connections")

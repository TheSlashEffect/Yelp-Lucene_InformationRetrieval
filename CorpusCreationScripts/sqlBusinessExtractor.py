#!/usr/bin/python
import sys
import datetime
import mysql.connector
from mysql.connector import errorcode

config = {
    'user': 'root',
    'password': 'allPiDecimalDigits',
    'database': 'yelp_db',
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

cursor0 = dbCon0.cursor(buffered=True)
cursor1 = dbCon1.cursor(buffered=True)

query = "SELECT * FROM business WHERE id IN (SELECT business_id FROM category WHERE category = 'Asian Fusion' or category = 'Cambodian' or category = 'Chinese' or category = 'Filipino' or category = 'Himalayan/Nepalese' or category = 'Hong Kong Style Cafe' or category = 'Indonesian' or category = 'Japanese' or category = 'Korean' or category = 'Laotian' or category = 'Live/Raw Food' or category = 'Malaysian' or category = 'Noodles' or category = 'Pan Asia' or category = 'Polynesian' or category = 'Singaporean' or category = 'Sri Lankan' or category = 'Sushi Bars' or category = 'Thai' or category = 'Vietnamese' or category = 'Arabian' or category = 'Armenian' or category = 'Bangladeshi' or category = 'Burmese' or category = 'Indian' or category = 'Kebab' or category = 'Kosher' or category = 'Middle Eastern' or category = 'Pakistani' or category = 'Persian/Iranian' or category = 'Russian' or category = 'Syrian' or category = 'Taiwanese' or category = 'Turkish' or category = 'Uzbek' or category = 'Mexican');"

count = 0

startTime = datetime.datetime.now()

try:
    cursor0.execute(query)  # cursor0 now contains 'tuples' of countries {CountryName, CountryCode}

    for row in cursor0:
        count += 1
        businessId = str(row[0]) + '\n'
        filename = str(row[1]) + '\n'
        neighborhood = str(row[2]) + '\n'
        address = str(row[3]) + '\n'
        city = str(row[4]) + '\n'
        state = str(row[5]) + '\n'
        postCode = str(row[6]) + '\n'
        latitude = str(row[7]) + '\n'
        longitude = str(row[8]) + '\n'
        stars = str(row[9]) + '\n'
        revCount = str(row[10]) + '\n'

        # Remove illegal characters from filename
        filenameNorm = "".join([c for c in filename if c.isalpha() or c.isdigit() or c == ' ']).rstrip()

        # Create document with Restaurant's name as filename
        docFile = open('D:/Temp/yelp/business/' + str(count) + '. ' + filenameNorm + '.txt', 'w', encoding='utf-8')

        docFile.write(businessId)
        docFile.write(filename)
        docFile.write(neighborhood)
        docFile.write(address)
        docFile.write(city)
        docFile.write(state)
        docFile.write(postCode)
        docFile.write(latitude)
        docFile.write(longitude)
        docFile.write(stars)
        docFile.write(revCount)

        # Perform review query for each restaurant based on business_id.
        # To speed this method up by class orders, an indexed was added
        # on the field "business_id" in the "review" table.
        sub_query = """SELECT text FROM review WHERE business_id = '{}' """.format(row[0])

        # Write reviews to file
        cursor1.execute(sub_query)
        for review in cursor1:
            docFile.write(review[0])

        docFile.close()

        # Show progress in real-time to user
        if count % 100 == 0:
            print(str(count) + ' files successfully written to disk')


except mysql.connector.Error as error:
    print("Error: {}".format(error))

endTime = datetime.datetime.now()

print("Task completed in " + str(endTime - startTime))

print("Count: = " + str(count))

dbCon0.close()
dbCon1.close()
print("Closed db connections")

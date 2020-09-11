SELECT count(*) FROM category WHERE category = 'Chinese';

SELECT * FROM category;


SELECT count(DISTINCT business_id)
FROM business as b0, category as c0
WHERE (c0.category = 'Churros') and b0.id = c0.business_id;



#Business id's of all businesses in the our selected sample space
SELECT count(DISTINCT business_id)
FROM category
WHERE 	category = 'Asian Fusion' or
		category = 'Cambodian' or
		category = 'Chinese' or
	    category = 'Filipino' or
	    category = 'Himalayan/Nepalese' or
	    category = 'Hong Kong Style Cafe' or
	    category = 'Indonesian' or
	    category = 'Japanese' or
	    category = 'Korean' or
	    category = 'Laotian' or
	    category = 'Live/Raw Food' or
	    category = 'Malaysian' or
	    category = 'Noodles' or
	    category = 'Pan Asia' or
	    category = 'Polynesian' or
	    category = 'Singaporean' or
	    category = 'Sri Lankan' or
	    category = 'Sushi Bars' or
	    category = 'Thai' or
	    category = 'Vietnamese' or
	    category = 'Arabian' or
	    category = 'Armenian' or
	    category = 'Bangladeshi' or
	    category = 'Burmese' or
	    category = 'Indian' or
	    category = 'Kebab' or
	    category = 'Kosher' or
	    category = 'Middle Eastern' or
	    category = 'Pakistani' or
	    category = 'Persian/Iranian' or
	    category = 'Russian' or
	    category = 'Syrian' or
	    category = 'Taiwanese' or
	    category = 'Turkish' or
	    category = 'Uzbek' or
	    category = 'Mexican';




#Cursor0's Query
SELECT *
FROM business
WHERE id IN (SELECT business_id
			 FROM category
             WHERE category = 'Restaurants');
             
             
#Cursor1's Query
SELECT *
FROM review
WHERE business_id = '--6MefnULPED_I942VcFNA';

SELECT count(*)
FROM review
WHERE business_id = '--6MefnULPED_I942VcFNA';


SELECT *
FROM review
ORDER BY date DESC;

#Count of reviews for all businesses belonging in 'Restaurant' parent category
SELECT sum(review_count)
FROM business
WHERE id IN (SELECT DISTINCT business_id
			 FROM category
             WHERE category = 'Restaurants');


SELECT *
FROM review as r0, business as b0
WHERE b0.id = r0.id AND b0.id IN (SELECT DISTINCT business_id
								  FROM category
								  WHERE category = 'Restaurants')
LIMIT 100;



#Statistic information regarging reviews for all Asian/Mexican Restaurants
SELECT max(review_count)
FROM business
WHERE id IN (SELECT DISTINCT business_id
			 FROM category
             WHERE category = 'Asian Fusion' or
			 	   category = 'Cambodian' or
				   category = 'Chinese' or
				   category = 'Filipino' or
				   category = 'Himalayan/Nepalese' or
				   category = 'Hong Kong Style Cafe' or
				   category = 'Indonesian' or
				   category = 'Japanese' or
				   category = 'Korean' or
				   category = 'Laotian' or
				   category = 'Live/Raw Food' or
				   category = 'Malaysian' or
				   category = 'Noodles' or
				   category = 'Pan Asia' or
				   category = 'Polynesian' or
				   category = 'Singaporean' or
				   category = 'Sri Lankan' or
				   category = 'Sushi Bars' or
				   category = 'Thai' or
				   category = 'Vietnamese' or
                   category = 'Arabian' or
                   category = 'Armenian' or
                   category = 'Bangladeshi' or
                   category = 'Burmese' or
                   category = 'Indian' or
                   category = 'Kebab' or
                   category = 'Kosher' or
                   category = 'Middle Eastern' or
                   category = 'Pakistani' or
                   category = 'Persian/Iranian' or
                   category = 'Russian' or
                   category = 'Syrian' or
                   category = 'Taiwanese' or
                   category = 'Turkish' or
                   category = 'Uzbek' or
                   category = 'Mexican');


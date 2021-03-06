/*
 * Quelgues adresses de test sur les villes
 */

use eventual

db.createCollection('activities')

mongoimport --db eventual --collection activities --type csv --headerline --file cities.csv

// conversion en string
db.activities.find({zipcode : {$exists : true}}).forEach(
  function(x) {
   x.zipcode = "" + parseInt(x.zipcode);
   db.activities.save(x);
   }
);



/*
db.activities.find({"latitude":{$type:16}}).forEach(
  function(x) {
   x.latitude = parseFloat(parseInt(x.latitude) + ".0");
   db.activities.save(x);
   }
);
*/


db.activities.ensureIndex( { name:1 } )
db.activities.ensureIndex( { activity:1,latitude:1,longitude:1 } )

db.createCollection('sites')
db.activities.find().sort( {name:-1}).forEach(
    function(x) {
         db.sites.insert(
             {
              name: x.name,
              activity: x.activity,
              adress: x.adress,
              zipcode: x.zipcode,
              city: x.city,
              country: x.country,
              loc:{"lon": parseFloat(x.longitude), "lat": parseFloat(x.latitude)}
             }
         );
     }
  );


db.sites.ensureIndex( { name:1 } )
db.sites.ensureIndex( { loc: "2d" }  )


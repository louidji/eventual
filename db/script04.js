
use eventual





db.activities.ensureIndex( { name:1 } )

db.createCollection('sites')
db.activities.find().sort( {name:1}).forEach(
    function(x) {
         db.sites.insert(
             {
              name: x.name,
              activity: x.activity,
              adress: x.adress,
              zipcode: x.zipcode,
              city: x.city,
              country: x.country,
              loc: { type: "Point", coordinates: [ parseFloat(x.longitude), parseFloat(x.latitude) ] }
             }
         );
     }
  );


db.sites.ensureIndex( { name:1 } )
db.sites.ensureIndex( { loc: "2dsphere", activity:1 }  )


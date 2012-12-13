(ns sentimetrika.models.schema)

(def names #{:good :neutral :bad})

(def reviews-table
  [:reviews
   [:id :integer "PRIMARY KEY" "NOT NULL" "IDENTITY"]
   [:title "nvarchar(255)"]
   [:body "nvarchar(1000)" "NOT NULL"]])

(def tags-table
  [:tags
   [:id :integer "PRIMARY KEY" "NOT NULL" "IDENTITY"]
   [:review_id :integer]
   [:name "varchar(100)"]])

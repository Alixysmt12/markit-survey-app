Survey
    .StylesManager
    .applyTheme("modern");

var json = {
    questions: [
        {
            type: "file",
            title: "Please upload your photo",
            name: "image",
            storeDataAsText: true,
            showPreview: true,
            imageWidth: 150
        },
        {
             "type": "microphone",
             "name": "question2"
            },
                {
                 "type": "matrix",
                 "name": "Quality",
                 "title": "Please indicate if you agree or disagree with the following statements",
                 "columns": [
                  {
                   "value": 1,
                   "text": "Strongly Disagree"
                  },
                  {
                   "value": 2,
                   "text": "Disagree"
                  },
                  {
                   "value": 3,
                   "text": "Neutral"
                  },
                  {
                   "value": 4,
                   "text": "Agree"
                  },
                  {
                   "value": 5,
                   "text": "Strongly Agree"
                  }
                 ],
                 "rows": [
                  {
                   "value": "affordable",
                   "text": "Product is affordable"
                  },
                  {
                   "value": "does what it claims",
                   "text": "Product does what it claims"
                  },
                  {
                   "value": "better then others",
                   "text": "Product is better than other products on the market"
                  },
                  {
                   "value": "easy to use",
                   "text": "Product is easy to use"
                  }
                 ]
                },
                {
                 "type": "rating",
                 "name": "satisfaction",
                 "title": "How satisfied are you with the Product?",
                 "isRequired": true,
                 "minRateDescription": "Not Satisfied",
                 "maxRateDescription": "Completely satisfied"
                },
                {
                 "type": "rating",
                 "name": "recommend friends",
                 "visibleIf": "{satisfaction} > 3",
                 "title": "How likely are you to recommend the Product to a friend or co-worker?",
                 "minRateDescription": "Will not recommend",
                 "maxRateDescription": "I will recommend"
                },
                {
                 "type": "comment",
                 "name": "suggestions",
                 "title": "What would make you more satisfied with the Product?"
                },
                {
                 "type": "radiogroup",
                 "name": "question2",
                 "choices": [
                  "item1",
                  "item2",
                  "item3"
                 ]
                }4
    ]
};

window.survey = new Survey.Model(json);

survey
    .onAfterRenderQuestion
    .add(function (sender, options) {
        if(options.question.getType() === "file") {
            var input = options.htmlElement.querySelector("input");
            input.capture = "camera";
        }
    });


survey
    .onComplete
    .add(function (result) {
        document
            .querySelector('#surveyResult')
            .textContent = "Result JSON:\n" + JSON.stringify(result.data, null, 3);
    });


$("#surveyElement").Survey({model: survey});

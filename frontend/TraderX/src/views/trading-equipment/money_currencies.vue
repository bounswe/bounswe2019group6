<template>
 
  <div>
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <el-card class='raddar-chart-container'>
        <raddar-chart :chart-data="chartData"/>
        <div>
          <p  class="te-info-text">Categories are calculated as follows;</p>
          <p  class="te-info-text"><b>Stabilitity:</b> 1 / [ sum( | average - ith data | / average ) / 100 ] --> i:[0,100]</p>
          <p  class="te-info-text"><b>Growth:</b> sum( ith data - (i+1)th data ) / 100 --> i:[0,99]</p>
        </div>
      </el-card>
    </el-row>

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <el-card class='raddar-chart-container'>
        <p class="te-info-text">Last 20 days' opening values of every currency is as follows:</p>
        <line-chart-comparison :chart-data="comparisonData"/>
      </el-card>
    </el-row>

    <el-row :gutter="20" style="padding:16px 16px 0;margin-bottom:32px;">
      <el-card>
        <el-tabs v-model="activeTab">
          <el-tab-pane class='te-tab-pane' v-for="ed in equipmentData" :key="ed.key" :label="ed.label" :name="ed.key" :type="ed.key">
              <div v-if="activeTab==ed.key">
                <p class="te-info-text">Last 100 days' American Dollar / {{ ed.label }} with openning values is as follows:</p>
                <div class='chart-wrapper'>
                  <line-chart :type="ed.key" :chart-data="ed.data"/>
                </div>

                <p class="te-info-text">Last 20 days' American Dollar / {{ ed.label }} with openning, closing, highest and lowest values is as follows:</p>
                <div class='chart-wrapper'>
                  <line-chart-detailed :type="ed.key" :chart-data="ed.data"/>
                </div>

                <el-button class='change-base-button' @click="changeBaseofEquipment(ed.label)"><svg-icon style="margin-right:10px" icon-class="chart" />Change the Base</el-button>
                <el-button class='buy-button' @click="showBuyEquipmentDialog=true"><svg-icon style="margin-right:10px" icon-class="shopping" />Buy Equipment</el-button>
                
                <el-card class='container-in-tab'>
                  <p> alerts comes here </p>
                </el-card>

                <el-card class='container-in-tab'>
                  <h4> Most reviewed articles about {{ed.label}} </h4>

                  <!-- Got this from complex-table component -->
                  <el-table
                    :data="articleList"
                    border
                    fit
                    highlight-current-row
                    style="width: 100%;"
                  >
                    <el-table-column label="ID" prop="id" align="center">
                      <template slot-scope="scope">
                        <span>{{ scope.row.id }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="Date" align="center">
                      <template slot-scope="scope">
                        <span>{{ scope.row.timestamp | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
                      </template>
                    </el-table-column>
                    <!-- TODO: when pressed to one of the titles it should direct the user to the corresponding article -->
                    <el-table-column label="Title" >
                      <template slot-scope="{row}">
                        <span class="link-type">{{ row.title }}</span>
                        <!-- <el-tag>{{ row.type | typeFilter }}</el-tag> -->
                      </template>
                    </el-table-column>
                    <el-table-column label="Author" align="center">
                      <template slot-scope="scope">
                        <span>{{ scope.row.author }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="Importance" >
                      <template slot-scope="scope">
                        <svg-icon v-for="n in +scope.row.importance" :key="n" icon-class="star" class="meta-item__icon" />
                      </template>
                    </el-table-column>
                    <el-table-column label="Readings" align="center">
                      <template slot-scope="{row}">
                        <span v-if="row.pageviews" class="link-type">{{ row.pageviews }}</span>
                        <span v-else>0</span>
                      </template>
                    </el-table-column>
                  </el-table>
                  
                  <el-button class='read-article' style="margin-top:20px;" @click="readArticle(ed.label)"><svg-icon style="margin-right:10px" icon-class="shopping" /> Read More Articles about {{ ed.label }} </el-button>
                </el-card>

                <el-card class='container-in-tab'>
                  <h4> Comments about {{ ed.label }} </h4>
                  <el-button class='create-comment' style="margin-top:20px;margin-bottom:20px;" @click="showCreateCommentDialog=true">
                    <svg-icon style="margin-right:10px" icon-class="edit" /> Write Comment </el-button>
                  
                  <el-row class='row' v-for="c in commentList" v-bind:key="c.id" :gutter="20" style="padding:16px 16px 0;margin-bottom:20px;">
                    <el-card class='comment-container'>
                      <p>
                        <span class="comment-author"> {{ c.author }} </span>
                        <span class="comment-time"> {{ c.timestamp | parseTime('{y}-{m}-{d} {h}:{i}') }} - </span>
                        <span class="comment-options"> {{ c.likes }} Likes </span>
                      </p>
                      <p class="comment-text"> {{ c.comment }} </p>
                      <p class="comment-options">
                        <a @click="showReplyCommentDialog=true;replyCommentId=c.id"> Reply </a> |
                        <a @click="likeComment(c.id)"> <svg-icon icon-class="star" /> Like </a>
                      </p>
                      <div class="comment-replies" v-for="r in c.replies" v-bind:key="r.id" style="padding:8px 20px 0;margin-bottom:10px;">
                        <p>
                          <span class="comment-author"> {{ r.author }} </span>
                          <span class="comment-time"> {{ r.timestamp | parseTime('{y}-{m}-{d} {h}:{i}') }} - </span>
                          <span class="comment-options"> {{ r.likes }} Likes </span>
                        </p>
                        <p class="comment-text"> {{ r.comment }} </p>
                        <p class="comment-options">
                          <a @click="likeReplyComment(c.id, r.id)"> <svg-icon icon-class="star" /> Like </a>
                        </p>
                      </div>
                    </el-card>
                  </el-row>
                
                </el-card>

              </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </el-row>

    <el-dialog title="Select Trading Equipment" :visible.sync="showBuyEquipmentDialog">
      <el-input placeholder="Please enter an amount" v-model="buyamountinput" class="input-with-select">
        <el-select style="width:120px" v-model="select" slot="prepend" placeholder="Select">
          <div>
            <el-option v-for="(item, index) in this.equipmentData" :key="index" :label="item.key" :value="item.key"></el-option>
          </div>
        </el-select>
        <el-button slot="append" @click="buyEquipment()">Buy</el-button>
      </el-input>  
    </el-dialog>

    <el-dialog title="Create Comment" :visible.sync="showCreateCommentDialog">
      <textarea class="comment-textarea" placeholder="Write your comment here" cols="100" rows="10" v-model="createCommentContent"></textarea>
      <div>
        <el-button style="margin-top:10px;" @click="createComment()"><svg-icon icon-class="edit"/> Publish Comment </el-button>
      </div>
    </el-dialog>

    <el-dialog title="Reply Comment" :visible.sync="showReplyCommentDialog">
      <textarea class="comment-textarea" placeholder="Write your comment here" cols="100" rows="10" v-model="replyCommentContent"></textarea>
      <div> 
        <el-button style="margin-top:10px;" @click="replyComment(replyCommentId)"><svg-icon icon-class="edit"/> Reply </el-button>
      </div>
    </el-dialog>

  </div>
   
</template>

<script>
import Tinymce from '@/components/Tinymce'
import LineChart from './components/LineChart'
import LineChartDetailed from './components/LineChartDetailed'
import LineChartComparison from './components/LineChartComparison'
import RaddarChart from './components/RaddarChart'
import { buyEquipment } from '@/api/equipment'

// The usual sorting in javascript sorts alphabetically which causes mistake in our code
const numberSort = function (a,b) {
    return a - b;
};

export default {
  name: 'DashboardAdmin',
  components: {
    LineChartComparison,
    LineChartDetailed,
    LineChart,
    RaddarChart,
    Tinymce,
  },
  data() {
    return {
      chartData: {},
      showBuyEquipmentDialog: false,
      showCreateCommentDialog: false,
      showReplyCommentDialog: false,
      buyamountinput: '',
      select: '',
      createCommentContent: '',
      replyCommentContent: '',
      replyCommentId: '',
      // equipmentData is expected to be: 
      // [
      //   {
      //     label: Euro,
      //     key: EUR,
      //     data: {
      //       open: [...],
      //       close: [...],
      //       high: [...],
      //       low: [...],
      //       current: [...] 
      //     }
      //   }, 
      //   {
      //     label: Japanese Yen,
      //     key: JPY
      //   }, ...
      // ]
      equipmentData: [],
      comparisonData: {equipmentData: []},
      activeTab: 'JPY',
      articleList: null,
      commentList: null
    }
  },
  async created() {
    this.returnArticleList()
    this.returnCommentList()
    var equipmentList = await this.getEquipmentList()
    // Fill the equipmentData with equipment list
    equipmentList.forEach(function(equipmentKey) {
      this.equipmentData.push({key: equipmentKey.code})
    }, this)
    var equipmentValues = await this.getEquipmentValues(equipmentList) 
    this.createRadarChartData(equipmentValues)
    this.equipmentData = equipmentValues
    
    this.comparisonData.equipmentData = this.equipmentData
  },
  methods: {

    // For now it returns mock data
    // TODO: change this method to get actual articles
    returnArticleList() {
      this.articleList = [
        {
          id: 1,
          timestamp: 1024316325463,
          title: 'Trolollolo',
          author: 'Irmos',
          importance: 3,
          pageviews: 245
        },

        {
          id: 2,
          timestamp: 1012345625463,
          title: 'Random articlezz',
          author: 'Sado',
          importance: 2,
          pageviews: 12420425
        },

        {
          id: 3,
          timestamp: 1024316325463,
          title: 'adfniun biyg oiniug',
          author: 'Yukseljim',
          importance: 3,
          pageviews: 15
        },

        {
          id: 4,
          timestamp: 1024313478569,
          title: 'U picku materinu',
          author: 'More Irmos',
          importance: 1,
          pageviews: 945231
        },

        {
          id: 5,
          timestamp: 1024313456789,
          title: 'My hands are typing words',
          author: 'Irmos',
          importance: 2,
          pageviews: 8245
        }
      ]
    },

    returnCommentList() {
      this.commentList = [
        {
          id: 1,
          timestamp: 1024316325463,
          likes: 5,
          author: 'irmakguzey',
          comment: 'That’s the findings of Natixis Global Asset Management’s annual Global Portfolio Barometer, which has found that UK portfolios with significant non-sterling assets saw average performance of more than 13 per cent.',
          replies: [
            {
              id: 1,
              timestamp: 1024316325463,
              likes: 1,
              author: 'sadullahgultekin',
              comment: `I think
                        you are
                        quite correct!`
            },
            {
              id: 2,
              timestamp: 1024316325463,
              likes: 0,
              author: 'irmakguzey',
              comment: 'Thank you!'
            },
          ]
        },
        {
          id: 2,
          timestamp: 1056316325463,
          likes: 54,
          author: 'sadullahgultekin',
          comment: 'Matthew Riley, head of research at the portfolio research and consulting group, says: “A substantial part of the explanation is currency risk which is no surprise since currency moves in 2016 were the highest since 2008 and had a large impact on the surveyed portfolios.',
          replies: []
        },
        {
          id: 3,
          timestamp: 1234567890123,
          likes: 15,
          author: 'burakyuksel',
          comment: 'For example, he says, a UK investor with unhedged US equity exposure (in other words, without making compensating investments to counteract the risk) would have gained an extra 19 per cent return in 2016 due to the depreciation of the Pound versus the Dollar.\"For eurozone equities, this would have been around 16 per cent, and for Japanese equities this would have been 23 per cent. Currency impact was also seen in allocation funds, EM debt and high yield debt funds, which are often not hedged by advisers.\”',
          replies: []
        },
        {
          id: 4,
          timestamp: 1024457825463,
          likes: 500,
          author: 'irmakguzey',
          comment: 'A lot of people. Foreign exchange is most commonly known as Forex and Forex is the world’s most traded market. According to CityIndex there’s an average turnover in excess of US$5.3 trillion every single day. That’s 4.24 trillion pounds at time of writing, although as will be seen that can change. A lot of different people are trading, from large companies to part-time traders operating out of their bedrooms, something that only became possible with the proliferation of the internet.',
          replies: []
        },
        {
          id: 5,
          timestamp: 3456789341267,
          likes: 234,
          author: 'burakyuksel',
          comment: 'What drives currency movements? Most people already know that the values of currencies shift, that’s why exchange rates change. And the changes in those rates are determined by multitude of traders buying currencies with other currencies and making judgements on what each is worth in relation to each other. Prices can change at incredible speed in response to news and global events. Traders look at key factors, including political and economic stability, currency intervention, monetary policy and major events such as natural disasters. How does it work? When trading Forex, currencies come in pairs, for example, sterling/US dollar. The trader predicts how the exchange rate between the two currencies will change. So, if the trader believes that US dollars will strengthen against the pound then they buy dollars, which means they are also ditching their pounds. If they are right then the value of their currency rises and they can sell for a profit. If their hunch was wrong then they lose. For example, the GBP/USD rate shows the number of dollars one pound can buy. If a trader believes the pound will increase in value against the dollar then they use dollars to buy pounds. If the exchange rate rises then they can sell the pounds back for a profit. One of the reasons Forex trading is so popular with hobbyist investors is that the markets are open pretty much 24 hours a day, following the different countries’ time zones. Will I make any money? Forex is risky. It’s so risky that many commentators have likened home traders to professional gamblers, arguing that the idea an individual can reliably predict the movements of currencies is nonsense. There are an abundance of platforms and guides and books and investment tutorials that suggest it’s possible to make a small fortune trading currencies. However, spend any time reading forums and there are hoards of bedroom Forex traders losing money day after day. It can be very expensive to make currency transactions and individual traders usually don’t have a large enough pot to make anything other than small gains.',
          replies: []
        },
      ]
    },

    // Promise for getting equipments list 
    async getEquipmentList() {
      try {
        await this.$store.dispatch('equipment/getAllCurrencies')
        var res = this.$store.getters.currencyResult
        return res.equipments
      } catch (error) {
        console.log(error)
        return error
      }  
    },

    // equipment = ['JPY', 'TRY', ...]
    async getEquipmentValues(equipmentList) {
      // equipmentValues = [{label="Turkish Lira", key="TRY", data={open=[5.6, 4.34, 7.54,...], close=[..], high=[..], low=[..]}}, {label="Japanese Yen", key="JPY", data={actualData=[12.2312, 11.234545, 10.23234, ...]}} ...]
      var equipmentValues = []
      
      equipmentList.forEach(async function(e) {
        try {
          await this.$store.dispatch('equipment/getEquipment', e.code.toLowerCase())
          var res = this.$store.getters.equipmentQueryResult
          equipmentValues.push({})
          equipmentValues[equipmentValues.length-1].key = res.equipment.code
          equipmentValues[equipmentValues.length-1].label = res.equipment.name
          equipmentValues[equipmentValues.length-1].currentValue = res.equipment.currentValue
          equipmentValues[equipmentValues.length-1].data = {
            open: [],
            close: [],
            high: [],
            low: [],
            current: [],
          }
          res.historicalValues.forEach(function(val) {
            equipmentValues[equipmentValues.length-1].data.open.push(val.open)
            equipmentValues[equipmentValues.length-1].data.close.push(val.close)
            equipmentValues[equipmentValues.length-1].data.high.push(val.high)
            equipmentValues[equipmentValues.length-1].data.low.push(val.low)
            equipmentValues[equipmentValues.length-1].data.current.push(res.equipment.currentValue)
          })
        } catch (error) {
          console.log(error)
        }
      }, this)
      return equipmentValues
    },

    // Put values from 1 to 3 to each of equipment in categories: Stability, Growth Rate, Values
    createRadarChartData(equipmentValues) {
      // wait for data to be pulled properly
      if (equipmentValues.length == 0) {
        setTimeout(() => {
          this.createRadarChartData(equipmentValues)
        }, 500)
      } else {
        this.activeTab = equipmentValues[0].key
        var stabilities = [] // calculate stability of each trading equipment
        var growth = [] // calculate growth rate for each equipment
        var values = []
        var legendData = [] // For getting legendData
        var graphData = []

        const equipmentSize = equipmentValues.length
        for (let i = 0; i < equipmentSize; i++) {
          let valueAverage = 0
          equipmentValues[i].data.open.forEach(function(val) {
            valueAverage += val / equipmentValues[i].data.open.length
          })
          let stability = Math.abs(valueAverage - equipmentValues[i].data.open[0]) / equipmentValues[i].data.open.length
          let growthRate = 0
          for (let j = 0; j < equipmentValues[i].data.open.length-1; j++) {
            stability += (Math.abs(valueAverage - equipmentValues[i].data.open[j+1]) / valueAverage) / equipmentValues[i].data.open.length
            growthRate += (1/equipmentValues[i].data.open[j] - 1/equipmentValues[i].data.open[j+1]) / equipmentValues[i].data.open.length
          }
          stabilities.push(1/stability)
          growth.push(growthRate)
          values.push(1/equipmentValues[i].currentValue)
          legendData.push(equipmentValues[i].label)
          graphData.push({value: [1/stability, growthRate, 1/equipmentValues[i].currentValue], name: equipmentValues[i].label})
        }
        stabilities.sort(numberSort)
        growth.sort(numberSort)
        values.sort(numberSort)
        for (let i = 0; i < equipmentSize; i++) {
          graphData[i].value = [stabilities.indexOf(graphData[i].value[0])+1, growth.indexOf(graphData[i].value[1])+1, values.indexOf(graphData[i].value[2])+1]
        }
        var indicatorData = [
          { name: 'Stability', max: equipmentSize+0.5 },
          { name: 'Growth', max: equipmentSize+0.5 },
          { name: 'Value', max: equipmentSize+0.5 }
        ]
        this.chartData = {
          legendData: legendData,
          graphData: graphData,
          indicatorData: indicatorData
        }
      }
     
    },

    buyEquipment() {
      this.$store.dispatch('equipment/buyEquipment', {'code' : this.select, "amount" : this.buyamountinput}).then(response => {
        this.showBuyEquipmentDialog = false
        this.buyamountinput =  ''
        this.$message.success('Equipment Is Bought Successfully!')
      }).catch(err => {
        console.log(err)
      })
    },

    changeBaseofEquipment(equipmentLabel) {
      this.equipmentData.forEach(function(currency) {
        if (currency.label == equipmentLabel) {
          for (let i = 0; i < currency.data.open.length; i++) {
            currency.data.open[i] = 1/currency.data.open[i]
            currency.data.close[i] = 1/currency.data.close[i]
            currency.data.low[i] = 1/currency.data.low[i]
            currency.data.high[i] = 1/currency.data.high[i]
            currency.data.current[i] = 1/currency.data.current[i]
          }
        }
      }, this)
    },

    readArticle(equipmentLabel) {
      this.$notify({
        title: 'Success',
        message: 'Directing you to the articles about ' + equipmentLabel,
        type: 'success',
        duration: 2000
      })
    },

    //  TODO: This should be done at the backend
    likeComment(commentId) {
      this.commentList.forEach(function(comment) {
        if (comment.id == commentId) {
          comment.likes += 1;
        }
      }, this)
    },

    likeReplyComment(commentId, replyId) {
      this.commentList.forEach(function(comment) {
        if (comment.id == commentId) {
          comment.replies.forEach(function(reply){
            if (reply.id == replyId) {
              reply.likes += 1;
            }
          })
        }
      }, this)
    },

    // TODO: This should be done at the backend
    replyComment(commentId) {
      this.commentList.forEach(function(comment) {
        if (comment.id == commentId) {
          var newReply = {
            id: comment.replies.length+1,
            timestamp: Date.now(),
            likes: 0,
            author: 'current-user',
            comment: this.replyCommentContent
          }
          comment.replies.push(newReply)
        }
      }, this)
        this.showReplyCommentDialog = false
        this.replyCommentContent = ''
    },

    // TODO connect this to backend
    createComment() {
      var newComment = {
        id: this.commentList.length+1,
        timestamp: Date.now(),
        likes: 0,
        author: 'current-user', // TODO: change this with backend added
        comment: this.createCommentContent,
        replies: []
      }
      this.commentList.push(newComment)
      this.showCreateCommentDialog = false
      this.createCommentContent = ''
    }
  }
}
</script>

<style lang="scss" scoped>

.raddar-chart-container {
  margin-top: 10;
}

.container-in-tab {
  margin-top: 30px;
}

.comment-time {
  font-weight: normal;
  font-size: 8pt;
  color:#696969;
}

.comment-author {
  font-weight: bold;
  font-size: 11pt;
}

.comment-text {
  font-weight: normal;
  font-size: 10pt;
  color: #404040;
  white-space: pre-line;
}

.comment-options {
  font-weight: normal;
  font-size: 9pt;
  color: #696969
}

.star {
  background-color: #696969;
}

.comment-textarea {
  resize: none;
}

</style>

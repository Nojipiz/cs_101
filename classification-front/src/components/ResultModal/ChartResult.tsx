import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Pie } from 'react-chartjs-2';

ChartJS.register(ArcElement, Tooltip, Legend);

interface Props{
    data1:number,
    data2: number
}



const ChartResult: React.FC<Props> = ({data1, data2}) => {
    const mydata = {
        labels: ['Error', 'Fiabilidad'],
        datasets:[{
            backgroundColor:['#CB3234', '#2d728f'],
            data:[data1,data2]
        }]
    }
    const options = {
        responsive:true
    }
    return(
        <div>
            <Pie 
                data = {mydata}
                options = {options}
            />
    
        </div>
        
    );
}

export default ChartResult;